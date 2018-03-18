package com.schedule.star.admin.component;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.schedule.star.admin.component.gotone.MailPhoneComponent;
import com.schedule.star.admin.component.router.ExecutorRouter;
import com.schedule.star.admin.component.router.RouterFactory;
import com.schedule.star.admin.dao.GroupDao;
import com.schedule.star.admin.dao.JobDao;
import com.schedule.star.admin.dao.LogDao;
import com.schedule.star.admin.entity.ExecutorEntity;
import com.schedule.star.admin.entity.JobEntity;
import com.schedule.star.admin.entity.LogEntity;
import com.schedule.star.core.bean.Result;
import com.schedule.star.core.rpc.RpcClientProxy;
import com.schedule.star.core.rpc.bean.TriggerParam;
import com.schedule.star.core.rpc.service.TriggerService;
import com.schedule.star.core.util.IDGenerator;
import com.schedule.star.core.util.R;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author xiangnan
 * @date 2018/3/10 19:49
 */
@Component
public class JobTrigger {
    private final static Logger logger = LogManager.getLogger();

    @Resource
    private JobDao jobDao;

    @Resource
    private GroupDao groupDao;

    @Resource
    private LogDao logDao;

    @Resource
    private RouterFactory routerFactory;

    @Resource
    private MailPhoneComponent mailPhoneComponent;

    /**
     * quartz调度触发
     * @param context context
     */
    public void trigger(JobExecutionContext context) {
        JobKey jobKey = context.getTrigger().getJobKey();
        String jobId = jobKey.getName();
        String groupId = jobKey.getGroup();
        Date triggerTime = context.getScheduledFireTime();

        logger.debug("quartz trigger job, jobId: {}, groupId: {}, triggerTime: {}",
                jobId, groupId, DateUtil.format(triggerTime, DatePattern.NORM_DATETIME_PATTERN));

        trigger(jobId, groupId, triggerTime, null);
    }

    public void trigger(String jobId, String groupId, Date time) {
        trigger(jobId, groupId, time, null);
    }

    /**
     * @param jobId jobId
     * @param groupId groupId
     * @param time 任务执行时间点
     * @param childJobs 子任务列表，可为空，以,分隔 R.partFlag.childJob
     */
    public void trigger(String jobId, String groupId, Date time, String childJobs) {
        JobEntity jobEntity = jobDao.selectByJobId(jobId);
        if (jobEntity == null) {
            logger.warn("not found job, jobId: {}", jobId);
            return;
        }

        if (StrUtil.isBlank(groupId)) {
            groupId = jobEntity.getGroupId();
        }

        TriggerParam param = new TriggerParam();
        param.setLogId(IDGenerator.getId());
        param.setJobId(jobEntity.getJobId());
        param.setJobName(jobEntity.getName());
        param.setJobType(R.jobType.Python);
        param.setScriptSource("print 'hello'");
        param.setChildJobs(childJobs);

        LogEntity logEntity = new LogEntity(param.getLogId(), jobId);
        logEntity.setTime(time);
        logEntity.setParams(ArrayUtil.isNotEmpty(param.getParams()) ?
                CollectionUtil.join(Arrays.asList(param.getParams()), R.partFlag.jobParam) : null);

        boolean loopTrigger = false;
        do {
            // 路由策略
            List<ExecutorEntity> executorEntityList = groupDao.selectExecutorListByGroupId(groupId);
            ExecutorRouter router = routerFactory.get(jobEntity.getRouterStrategy());
            ExecutorEntity executorEntity = router.router(executorEntityList);

            if (executorEntity == null) {
                logger.warn("executorList null, trigger {}[{}] groupId={}, time={}, childJobs={}",
                        jobEntity.getName(), jobId, groupId, time, childJobs);

                logEntity.setResult(R.status.FAIL);
                logEntity.setIpPort("null");
                logEntity.setContent("executor list null");
            } else {
                logEntity.setIpPort(executorEntity.getIp() + ":" + executorEntity.getPort());

                Result result = rpcTrigger(param, executorEntity);
                if (!result.success()) {
                    switch (jobEntity.getFailStrategy()) {
                        case R.failStrategy.RETRY: {
                            logger.warn("trigger {}[{}] fail, e={} try again",
                                    jobEntity.getName(), jobId, result.getData());

                            // loopTrigger最多2次
                            loopTrigger = !loopTrigger;
                        }
                        case R.failStrategy.ALARM:
                        case R.failStrategy.NOCARE:
                        default: {
                            break;
                        }
                    }
                }

                logEntity.setResult(result.getStatus());
                logEntity.setContent(result.getData().toString());
            }

        } while (loopTrigger);

        logger.info("trigger {}[{}], logId={}, result={}", jobEntity.getName(), jobId,
                logEntity.getLogId(), logEntity.getResult());

        if (!logEntity.triggerSuccess() && !StrUtil.equals(jobEntity.getFailStrategy(), R.failStrategy.NOCARE)) {
            // 报警处理
            String content = StrUtil.format("调度任务{}失败, jobId={}, logId={}, 原因={}",
                    jobEntity.getName(), jobId, logEntity.getLogId(), logEntity.getContent());
            mailPhoneComponent.sendMessage(jobEntity.getEmailPhone(), content);
        }

        // 记录任务调度结果
        logDao.insert(logEntity);
    }

    private Result rpcTrigger(TriggerParam param, ExecutorEntity executor) {
        try {
            TriggerService triggerService = (TriggerService) new RpcClientProxy(TriggerService.class,
                    "http://" + executor.getIp() + ":" + executor.getPort()).getObject();
            return triggerService.run(param);
        } catch (Throwable e) {
            logger.error("rpcTrigger error, e: {}", e);

            // rpc调用异常，可能是网络原因或者执行器挂了，这里先不把执行器设置为下线状态，
            // 由执行器状态检测线程把执行器设置为下线状态。
            return Result.fail(e);
        }
    }

}

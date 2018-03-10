package com.schedule.star.admin.component;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.schedule.star.admin.dao.ExecutorDao;
import com.schedule.star.admin.dao.JobDao;
import com.schedule.star.admin.entity.ExecutorEntity;
import com.schedule.star.admin.entity.JobEntity;
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
import java.util.Date;

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
    private ExecutorDao executorDao;

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
            logger.error("not found job, jobId: {}", jobId);
            return;
        }

        if (StrUtil.isBlank(groupId)) {
            groupId = jobEntity.getGroupId();
        }

        TriggerParam param = new TriggerParam();

        param.setJobId(jobEntity.getJobId());
        param.setJobName(jobEntity.getName());
        param.setLogId(IDGenerator.getId());
        param.setJobType(R.jobType.Python);
        param.setScriptSource("print 'hello'");

        ExecutorEntity executorEntity = executorDao.selectByExecutorId("1801312258145458192.168.236");

        try {
            TriggerService triggerService = (TriggerService) new RpcClientProxy(TriggerService.class,
                    "http://" + executorEntity.getIp() + ":" + executorEntity.getPort()).getObject();

            triggerService.run(param);
        } catch (Exception e) {
            logger.error("rpcTrigger error, e: {}", e);

            // rpc调度发生错误，将执行器设置为下线
//            executorEntity.setExecutorId(executorEntity.getExecutorId());
//            executorEntity.setStatus(R.executorStatus.OFFLINE);
//            executorDao.updateByExecutorId(executorEntity);
        }

    }

}

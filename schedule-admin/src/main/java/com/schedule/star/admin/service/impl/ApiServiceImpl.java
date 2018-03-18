package com.schedule.star.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.schedule.star.admin.component.JobTrigger;
import com.schedule.star.admin.component.gotone.MailPhoneComponent;
import com.schedule.star.admin.dao.ExecutorDao;
import com.schedule.star.admin.dao.JobDao;
import com.schedule.star.admin.dao.LogDao;
import com.schedule.star.admin.entity.ExecutorEntity;
import com.schedule.star.admin.entity.JobEntity;
import com.schedule.star.admin.entity.LogEntity;
import com.schedule.star.admin.service.ApiService;
import com.schedule.star.core.bean.CallbackParam;
import com.schedule.star.core.bean.RegisterParam;
import com.schedule.star.core.bean.Result;
import com.schedule.star.core.util.R;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xiangnan
 * @date 2018/3/10 10:02
 */
@Service
public class ApiServiceImpl implements ApiService {
    private static final Logger logger = LogManager.getLogger();

    @Resource
    private ExecutorDao executorDao;

    @Resource
    private JobDao jobDao;

    @Resource
    private LogDao logDao;

    @Resource
    private JobTrigger jobTrigger;

    @Resource
    private MailPhoneComponent mailPhoneComponent;

    @Override
    public Result register(RegisterParam param) {
        ExecutorEntity entity = ExecutorEntity.build(param);
        if (entity == null) {
            // not care
            return Result.FAIL;
        }

        logger.info("执行器注册成功: {}:{}", entity.getIp(), entity.getPort());
        logger.info("执行器将开始保活: {}:{}", entity.getIp(), entity.getPort());

        ExecutorEntity oldEntity = executorDao.selectByIpPort(param.getIp(), param.getPort());
        if (oldEntity != null) {
            return executorDao.updateByIpPort(entity) > 0 ? Result.SUCCESS : Result.FAIL;
        } else {
            return executorDao.insert(entity) > 0 ? Result.SUCCESS : Result.FAIL;
        }
    }

    @Override
    public Result keepAlive(String ip, int port) {

        ExecutorEntity entity = new ExecutorEntity(ip, port);
        entity.setStatus(R.executorStatus.ONLINE);

        return executorDao.updateByIpPort(entity) > 0 ? Result.SUCCESS : Result.FAIL;
    }

    @Override
    public Result callback(CallbackParam param) {
        LogEntity logEntity = logDao.selectByLogId(param.getLogId());
        JobEntity jobEntity = jobDao.selectByJobId(param.getJobId());
        if ((logEntity == null) || (jobEntity == null)) {
            logger.warn("not found log, logId: {}", param.getLogId());
            return Result.FAIL;
        }

        if (StrUtil.isNotBlank(param.getHandleResult())
                && StrUtil.equals(param.getHandleResult(), logEntity.getHandleResult())) {
            // 已经接收到callback结果了，直接返回
            return Result.success(R.callback.CAN_DELETE);
        }

        logger.info("callback, {}[{}], logId={}, result={}",
                jobEntity.getName(), param.getJobId(), param.getLogId(), param.getHandleResult());

        int update = logDao.updateHandleInfo(param.getHandleResult(), param.getHandleContent(),
                param.getHandleTime(), param.getFinishTime(), param.getLogId());
        if (update != 1) {
            // 更新失败或者未找到该log记录，则返回失败消息，让执行器下次重新发送
            logger.warn("not found log, logId: {}", param.getLogId());
            return Result.FAIL;
        }

        if (param.success()) {
            if (StrUtil.isNotBlank(param.getChildJobs())) {
                logger.info("execute child jobs by callback, jobs={}", param.getChildJobs());

                List<String> jobIdList = CollectionUtil.newArrayList(StrUtil.split(param.getChildJobs(), R.partFlag.childJob));
                if (!jobIdList.isEmpty()) {
                    String childJobId = jobIdList.remove(0);
                    JobEntity childJob = jobDao.selectByJobId(childJobId);
                    if ((childJob == null) || !StrUtil.equals(childJob.getStatus(), R.jobStatus.RUNNING)) {
                        logger.warn("callback trigger job, but job={} is not RUNNING. time={}, childJobs={}",
                                childJob, logEntity.getTime(), param.getChildJobs());
                    } else {
                        // callback触发任务调度
                        jobTrigger.trigger(jobEntity.getJobId(), jobEntity.getGroupId(), logEntity.getTime(), param.getChildJobs());
                    }
                }
            } else if (StrUtil.isNotBlank(jobEntity.getChildJobs())) {
                logger.info("execute child jobs, jobs={}", jobEntity.getChildJobs());

                List<String> topoChildIdList = generateTopoChildIdList(jobEntity);
                String childJobId = topoChildIdList.remove(0);
                jobTrigger.trigger(childJobId, null, logEntity.getTime(),
                        CollectionUtil.join(topoChildIdList, R.partFlag.jobParam));
            }
        } else {
            // 报警处理
            String content = StrUtil.format("任务{}执行失败, jobId={}, logId={}, 原因={}",
                    param.getJobName(), param.getJobId(), param.getLogId(), param.getHandleContent());
            mailPhoneComponent.sendMessage(jobEntity.getEmailPhone(), content);

            // 如果失败重试则重新进行一次调度
            if (StrUtil.equals(jobEntity.getFailStrategy(), R.failStrategy.RETRY)) {
                jobTrigger.trigger(jobEntity.getJobId(), jobEntity.getGroupId(), logEntity.getTime(), param.getChildJobs());
            }
        }

        return Result.success(R.callback.CAN_DELETE);
    }

    /**
     * 拓扑排序获取子任务列表，只要子任务之间没有形成有向图即可
     */
    private List<String> generateTopoChildIdList(JobEntity parentJob) {

        // 拓扑排序子任务链
        List<String> topoIdList = new LinkedList<>();
        List<String> jobList = new LinkedList<>();
        jobList.add(parentJob.getJobId());

        while (!jobList.isEmpty()) {
            JobEntity entity = jobDao.selectByJobId(jobList.remove(0));
            if (entity == null) {
                continue;
            }

            List<String> tmpList = StrUtil.isNotBlank(entity.getChildJobs()) ?
                    CollectionUtil.newArrayList(StrUtil.split(entity.getChildJobs(), R.partFlag.childJob)) : null;
            if (tmpList != null) {
                for (String id : tmpList) {
                    if (topoIdList.contains(id)) {
                        topoIdList.remove(id);
                    }
                    topoIdList.add(id);
                }

                jobList.addAll(tmpList);
            }
        }

        return topoIdList;
    }

}

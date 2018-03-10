package com.schedule.star.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.schedule.star.admin.bean.JobBean;
import com.schedule.star.admin.bean.convert.BaseConvert;
import com.schedule.star.admin.component.JobScheduler;
import com.schedule.star.admin.dao.JobDao;
import com.schedule.star.admin.entity.JobEntity;
import com.schedule.star.admin.exception.JobException;
import com.schedule.star.admin.exception.enums.JobEnum;
import com.schedule.star.admin.service.JobService;
import com.schedule.star.core.util.IDGenerator;
import com.schedule.star.core.util.R;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author xiangnan
 * @date 2018/3/10 19:14
 */
@Service
public class JobServiceImpl implements JobService {
    private final static Logger logger = LogManager.getLogger();

    @Resource
    private JobScheduler jobScheduler;

    @Resource
    private JobDao jobDao;

    @Override
    @Transactional
    public int postJob(JobBean jobBean) {
        if ((jobBean == null) || StrUtil.isNotBlank(jobBean.getJobId())) {
            throw new JobException(JobEnum.PARAM_ERROR);
        }

        JobEntity entity = JobConvert.toEntity(jobBean);
        entity.setJobId(IDGenerator.getId());
        entity.setStatus(R.jobStatus.CREATED);

        try {
            jobDao.insert(entity);

            if (StrUtil.isNotBlank(entity.getCron())) {
                // cron非空添加到quartz中
                jobScheduler.addJob(entity.getJobId(), entity.getGroupId(), entity.getCron());

                // 更新任务状态为运行
                entity.setStatus(R.jobStatus.RUNNING);
                jobDao.update(entity);
            }

            logger.info("添加任务成功, jobId: {}, jobName: {}", entity.getJobId(), entity.getName());
        } catch (Exception e) {
            logger.info("添加任务失败, jobId: {}, jobName: {}", entity.getJobId(), entity.getName());
            throw new JobException(JobEnum.UNKNOWN_ERROR, e.getMessage());
        }

        return 1; // :)
    }

    @Override
    @Transactional
    public int putJob(JobBean jobBean) {
        if (jobBean == null) {
            throw new JobException(JobEnum.PARAM_ERROR);
        }

        JobEntity entity = JobConvert.toEntity(jobBean);
        JobEntity oldEntity = jobDao.selectByJobId(entity.getJobId());
        if ((oldEntity == null) || !StrUtil.equals(entity.getGroupId(), entity.getGroupId())) {
            throw new JobException(JobEnum.PARAM_ERROR);
        }

        try {
            if (!StrUtil.equals(entity.getCron(), oldEntity.getCron())) {
                if (StrUtil.isNotBlank(oldEntity.getCron())) {
                    jobScheduler.deleteJob(oldEntity.getJobId(), oldEntity.getGroupId());
                }
                if (StrUtil.isNotBlank(entity.getCron())) {
                    jobScheduler.addJob(entity.getJobId(), entity.getGroupId(), entity.getCron());
                    entity.setStatus(R.jobStatus.RUNNING);
                }
            }

            jobDao.update(entity);
            logger.info("更新任务成功，jobId: {}, jobName: {}", entity.getJobId(), entity.getName());
        } catch (Exception e) {
            logger.error("更新任务失败，jobId: {}, e: {}", entity.getJobId(), e);
            throw new JobException(JobEnum.UNKNOWN_ERROR, e.toString());
        }

        return 1;
    }

    @Override
    public int executeJob(String jobId) {
        return 0;
    }

    @Override
    public int triggerJob(String jobId) {
        return 0;
    }

    @Override
    public int resumeJob(String jobId) {
        return 0;
    }

    @Override
    public int pauseJob(String jobId) {
        return 0;
    }

    @Override
    public int deleteJob(String jobId) {
        return 0;
    }

    static class JobConvert extends BaseConvert {

        static JobEntity toEntity(JobBean bean) {
            return mapper.map(bean, JobEntity.class);
        }

        static JobBean toBean(JobEntity entity) {
            return mapper.map(entity, JobBean.class);
        }

    }

}

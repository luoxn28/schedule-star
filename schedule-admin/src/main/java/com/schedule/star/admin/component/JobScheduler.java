package com.schedule.star.admin.component;

import cn.hutool.core.util.StrUtil;
import com.schedule.star.admin.component.bean.QzJobBean;
import com.schedule.star.admin.exception.JobException;
import com.schedule.star.admin.exception.enums.JobEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xiangnan
 * @date 2018/3/10 19:43
 */
@Component
public class JobScheduler {
    private final static Logger logger = LogManager.getLogger();

    @Resource(name = "internalScheduler")
    private Scheduler scheduler;

    public void addJob(String jobId, String groupId, String cron) {

        if (StrUtil.isBlank(jobId) || StrUtil.isBlank(groupId) || StrUtil.isBlank(cron)) {
            throw new JobException(JobEnum.PARAM_ERROR);
        }

        try {
            if (exists(jobId, groupId)) {
                return;
            }

            // cron
            CronScheduleBuilder cronScheduleBuilder =
                    CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
            CronTrigger cronTrigger =
                    TriggerBuilder.newTrigger().withIdentity(jobId, groupId).withSchedule(cronScheduleBuilder).build();

            // 添加任务
            JobDetail jobDetail = JobBuilder.newJob(QzJobBean.class).withIdentity(jobId, groupId).build();
            scheduler.scheduleJob(jobDetail, cronTrigger);

            logger.info("quartz添加任务成功: jobDetail: {}", jobDetail);
        } catch (SchedulerException e) {
            logger.info("quartz添加任务失败, quartz未知错误: jobId: {}, cron: {}", jobId, cron);
            throw new JobException(JobEnum.QUARTZ_ERROR);
        } catch (Exception e) {
            logger.info("quartz添加任务失败, cron表达式错误: jobId: {}, cron: {}", jobId, cron);
            throw new JobException(JobEnum.CRON_ERROR);
        }

    }

    public void triggerJob(String jobId, String groupId) {
        try {
            if (!exists(jobId, groupId)) {
                logger.warn("未找到quartz任务: jobId: {}, groupId: {}", jobId, groupId);
                return;
            }

            JobKey jobKey = new JobKey(jobId, groupId);
            scheduler.triggerJob(jobKey);
            logger.info("quartz触发任务成功，jobKey: {}", jobKey);
        } catch (Exception e) {
            logger.info("quartz触发任务失败, Quartz错误: " + e);
            throw new JobException(JobEnum.QUARTZ_ERROR);
        }
    }

    public void pauseJob(String jobId, String groupId) {
        try {
            if (!exists(jobId, groupId)) {
                logger.warn("未找到quartz任务: jobId: {}, groupId: {}", jobId, groupId);
                return;
            }

            JobKey jobKey = new JobKey(jobId, groupId);
            scheduler.triggerJob(jobKey);
            logger.info("quartz暂停任务成功，jobKey: {}", jobKey);
        } catch (Exception e) {
            logger.info("quartz暂停任务失败, Quartz错误: " + e);
            throw new JobException(JobEnum.QUARTZ_ERROR);
        }
    }

    public void resumeJob(String jobId, String groupId) {
        try {
            if (!exists(jobId, groupId)) {
                logger.warn("未找到quartz任务: jobId: {}, groupId: {}", jobId, groupId);
                return;
            }

            JobKey jobKey = new JobKey(jobId, groupId);
            scheduler.triggerJob(jobKey);
            logger.info("quartz恢复任务成功，jobKey: {}", jobKey);
        } catch (Exception e) {
            logger.info("quartz恢复任务失败, Quartz错误: " + e);
            throw new JobException(JobEnum.QUARTZ_ERROR);
        }
    }

    public void deleteJob(String jobId, String groupId) {
        try {
            if (!exists(jobId, groupId)) {
                logger.warn("未找到quartz任务: jobId: {}, groupId: {}", jobId, groupId);
                return;
            }

            TriggerKey triggerKey = new TriggerKey(jobId, groupId);
            scheduler.unscheduleJob(triggerKey);
            logger.info("quartz删除任务成功，triggerKey: {}", triggerKey);
        } catch (Exception e) {
            logger.info("quartz删除任务失败, Quartz错误: " + e);
            throw new JobException(JobEnum.QUARTZ_ERROR);
        }
    }

    private boolean exists(String jobId, String groupId) throws SchedulerException {
        return scheduler.checkExists(new TriggerKey(jobId, groupId));
    }

}

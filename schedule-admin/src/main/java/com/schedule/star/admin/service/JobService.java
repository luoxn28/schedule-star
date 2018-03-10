package com.schedule.star.admin.service;

import com.schedule.star.admin.bean.JobBean;

/**
 * @author xiangnan
 * @date 2018/3/10 19:14
 */
public interface JobService {

    /*
     * 任务操作接口
     */

    int postJob(JobBean jobBean);

    int putJob(JobBean jobBean);

    int executeJob(String jobId);

    int triggerJob(String jobId);

    int resumeJob(String jobId);

    int pauseJob(String jobId);

    int deleteJob(String jobId);

}

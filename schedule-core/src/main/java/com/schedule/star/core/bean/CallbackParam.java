package com.schedule.star.core.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xiangnan
 * @date 2018/3/10 7:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallbackParam implements Serializable {
    private String jobId;
    private String jobName;
    private String logId;
    private String handleResult;
    private String handleContent;
    private Date   handleTime;    // 任务执行开始时间
    private Date   finishTime;    // 任务执行结束时间
    private String childJobs;     // 子任务列表，可为空，以,分隔 R.partFlag.childJob

    public CallbackParam(String jobId, String logId) {
        this.jobId = jobId;
        this.logId = logId;
    }
}

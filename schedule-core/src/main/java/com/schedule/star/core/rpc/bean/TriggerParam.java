package com.schedule.star.core.rpc.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xiangnan
 * @date 2018/1/29 17:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TriggerParam implements Serializable {
    private static final long serialVersionUID = 7383349703914074460L;

    private String logId;
    private String jobId;
    private String jobName;

    private String jobType;       // 任务类型(Java/Script(Shell/Python))
    private String[] params;      // 任务参数

    private String javaHandler;   // Java任务handler
    private String scriptSource;  // Script任务源码

    private String childJobs;     // 子任务列表，可为空，以,分隔 R.partFlag.childJob
}

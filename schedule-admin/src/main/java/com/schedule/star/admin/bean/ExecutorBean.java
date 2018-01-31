package com.schedule.star.admin.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xiangnan
 * @date 2018/1/31 22:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutorBean {
    private String executorId;
    private String ip;
    private int port;
    private String name;
    private String status;         // 执行器当前状态，见 R.executorStatus
    private int keepAliveTime;     // 与调度器的保活时间（单位s）
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registerTime;
}

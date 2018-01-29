package com.schedule.star.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xiangnan
 * @date 2018/1/29 18:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutorEntity {
    private int id;
    private String executorId;
    private String ip;
    private int port;
    private String name;
    private String token;
    private String status;         // 执行器当前状态，见 R.executorStatus
    private int keepAliveTime;     // 与调度中心的保活时间（单位s）
    private Date registerTime;
    private Date updateTime;
}

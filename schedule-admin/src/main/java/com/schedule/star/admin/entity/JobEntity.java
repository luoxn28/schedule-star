package com.schedule.star.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xiangnan
 * @date 2018/3/10 18:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobEntity {
    private int    id;
    private String jobId;
    private String groupId;
    private String name;
    private String desc;
    private String type;
    private String cron;
    private String params;

    private String routerStrategy; // 路由策略
    private String failStrategy;   // 失败策略，包括调度失败，执行失败
    private String emailPhone;
    private String scriptLoc;
    private String className;

    private String paramCreator;   // 动态参数生成器
    private String paramRawValue;  // 动态参数原生值


    private String childJobs;      // 子任务，格式为 "任务id,任务id"
    private Date   createTime;
    private Date   updateTime;
    private String status;
}

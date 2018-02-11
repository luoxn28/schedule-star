package com.schedule.star.admin.exception.enums;

import lombok.AllArgsConstructor;

/**
 * @author xiangnan
 * @date 2018/2/11 21:59
 */
@AllArgsConstructor
public enum JobInfoEnum {

    UNKNOWN_ERROR(-1, "UNKNOWN_ERROR", "未知错误"),
    UNKNOWN_TASK_ERROR(-1, "UNKNOWN_ERROR", "未知任务类型"),
    QUARTZ_ERROR(-1, "QUARTZ_ERROR", "Quartz错误"),

    PARAM_ERROR(-1, "PARAM_ERROR", "参数错误"),
    JOB_EXIST(-1, "JOB_EXIST", "任务已存在"),
    TIMER_PARAM_ERROR(-1, "TIMER_PARAM_ERROR", "时间生成器参数错误"),

    PAGE_ERROR(-1, "PAGE_ERROR", "页数异常"),
    UPLOAD_ERROR(-1, "UPLOAD_ERROR", "未配置上传路径"),
    OVERMIND_ERROR(-1, "OVERMIND_ERROR", "未配置调度中心地址"),
    FILE_TYPE_ERROR(-1, "FILE_TYPE_ERROR", "不支持的文件类型"),

    CRON_ERROR(-1, "CRON_ERROR", "cron表达式错误");

    private int code;
    private String codeMsg;
    private String msg;

    public int getCode() {
        return code;
    }

    public String codeMsg() {
        return codeMsg;
    }

    public String getMsg() {
        return msg;
    }
}

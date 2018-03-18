package com.schedule.star.admin.entity;

import cn.hutool.core.util.StrUtil;
import com.schedule.star.core.util.R;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 任务日志
 *
 * @author xiangnan
 * @date 2018/3/18 14:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogEntity {
    private int    id;
    private String logId;
    private String jobId;

    private String ipPort;        // ip:port 格式
    private String result;        // 调度信息
    private String content;
    private Date   time;

    private String params;        // 执行参数

    private String handleResult;  // 执行信息
    private String handleContent;
    private Date   handleTime;
    private Date   finishTime;

    public LogEntity(String logId, String jobId) {
        this.logId = logId;
        this.jobId = jobId;
    }

    public boolean triggerSuccess() {
        return StrUtil.equals(result, R.status.SUCCESS);
    }
}

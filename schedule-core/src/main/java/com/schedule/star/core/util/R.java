package com.schedule.star.core.util;

/**
 * schedule-star 资源
 * @author xiangnan
 * @date 2018/1/28 15:33
 */
public interface R {

    interface status {
        String SUCCESS = "SUCCESS";
        String FAIL = "FAIL";
    }

    interface executorStatus {
        String ONLINE = "ONLINE";
        String OFFLINE = "OFFLINE";
        String DELETED = "DELETED";
    }

    interface groupStatus {
        String CREATED = "CREATED";
        String DELETED = "DELETED";
    }

    interface jobType {
        String Java = "Java";
        String Script = "Script";

        // 调度参数执行的任务类型
        String Shell = "Shell";
        String Python = "Python";
    }

    interface jobStatus {
        String CREATED = "CREATED";
        String RUNNING = "RUNNING";
        String PAUSE   = "PAUSE";
        String DELETED = "DELETED";
    }

    interface callback {
        String CAN_DELETE = "CAN_DELETE";
    }

    /**
     * 路由策略
     */
    interface routerStrategy {
        String RANDOM = "RANDOM"; /* 随机调度 */
        String FIRST  = "FIRST";  /* 第一个调度 */
    }

    /**
     * 失败处理策略（trigger失败、handle失败）
     */
    interface failStrategy {
        String NOCARE = "NOCARE"; // not care
        String ALARM  = "ALARM";  // 失败告警
        String RETRY  = "RETRY";  // 失败重试
    }

    interface partFlag {
        /**
         * 多个register url分隔符
         */
        String registerUrl = ",";

        /**
         * 多个jobId分隔符，子任务存储格式
         */
        String childJob = ",";

        /**
         * 多个任务参数分隔符
         */
        String jobParam = ",";

        /**
         * 多个email/phone分隔符
         */
        String emailPhone = ",";
    }

}

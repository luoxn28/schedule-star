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

    interface callback {
        String CAN_DELETE = "CAN_DELETE";
    }

    interface partFlag {
        /**
         * 多个register url分隔符
         */
        String registerUrl = ",";
    }

}

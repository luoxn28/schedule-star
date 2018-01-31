package com.schedule.star.admin.util;

import com.schedule.star.admin.bean.WebResultBean;

/**
 * @author xiangnan
 * @date 2018/1/31 22:40
 */
public class WebResultUtil {

    public static WebResultBean<Object> success(Object data) {
        return new WebResultBean<>(0, "success", data);
    }

    public static WebResultBean<Object> error(int code, Object data) {
        return new WebResultBean<>(code, "error", data);
    }

}

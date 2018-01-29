package com.schedule.star.core.util;

import cn.hutool.http.HttpRequest;

/**
 * @author xiangnan
 * @date 2018/1/29 19:56
 */
public class HttpUtil {

    public static byte[] postRpc(String url, byte[] param) {
        return HttpRequest.post(url)
                .body(param)
                .contentType("application/json")
                .timeout(10000)
                .execute()
                .bodyBytes();
    }

}

package com.schedule.star.core.util;

import cn.hutool.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

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

    public static byte[] readByte(HttpServletRequest request) throws IOException {

        request.setCharacterEncoding("UTF-8");
        int contentLen = request.getContentLength();
        InputStream in = request.getInputStream();

        if (contentLen > 0) {
            int readLenThisTime;
            int readLen = 0;
            byte[] data = new byte[contentLen];

            while (readLen != contentLen) {
                readLenThisTime = in.read(data, readLen, contentLen - readLen);
                if (readLenThisTime < 0) {
                    return new byte[] {};
                }
                readLen += readLenThisTime;
            }

            return data;
        }

        return new byte[] {};
    }

}

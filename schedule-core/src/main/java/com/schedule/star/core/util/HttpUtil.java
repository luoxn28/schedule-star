package com.schedule.star.core.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.schedule.star.core.bean.CallbackParam;
import com.schedule.star.core.bean.RegisterParam;
import com.schedule.star.core.bean.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author xiangnan
 * @date 2018/1/29 19:56
 */
public class HttpUtil {
    private static final Logger logger = LogManager.getLogger();

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

    public static Result register(String registerUrl, RegisterParam param) throws IOException {
        try {
            return rpcSend(registerUrl, param);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    public static Result callback(String callbackUrl, CallbackParam param) throws IOException {
        try {
            return rpcSend(callbackUrl, param);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    public static Result keepAlive(String url, String ip, int port) {
        try {
            if (!StrUtil.endWith(url, "/")) {
                url += "/";
            }

            HttpResponse response = HttpRequest.post(url + ip + "/" + port)
                    .contentType("application/json")
                    .timeout(10000)
                    .execute();
            if (response.getStatus() != 200) {
                return Result.fail(response.toString());
            }

            String body = response.body();
            if (StrUtil.isNotBlank(body) && StrUtil.startWith(body, "{")) {
                return JacksonUtil.toObject(body, Result.class);
            } else {
                return Result.fail("返回结果格式错误: " + body);
            }
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    private static Result rpcSend(String toUrl, Object param) throws IOException {
        try {
            HttpResponse response = postSend(toUrl, JacksonUtil.toJson(param));
            if (response.getStatus() != 200) {
                return Result.fail(response.toString());
            }

            String body = response.body();
            if (StrUtil.isNotBlank(body) && StrUtil.startWith(body, "{")) {
                return JacksonUtil.toObject(body, Result.class);
            } else {
                return Result.fail("返回结果格式错误: " + body);
            }
        } catch (Exception e) {
            logger.error("rpcSend error: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * post发送数据
     *
     * @param url http 路径
     * @param jsonParam http body 数据
     * @return 不同业务返回值可能不同
     */
    private static HttpResponse postSend(String url, String jsonParam) {
        return HttpRequest.post(url)
                .body(jsonParam)
                .contentType("application/json")
                .timeout(10000)
                .execute();
    }

}

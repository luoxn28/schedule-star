package com.schedule.star.core.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author xiangnan
 * @date 2018/1/29 17:10
 */
public class SystemUtil {

    // linux window 系统文件路径分隔符
    private static final char linuxSeparator = '/';
    private static final char windowSeparator = '\\';
    public static char systemSeparator = linuxSeparator;

    // 当前应用的运行目录
    public static String applicationPath = "";

    static {
        if (isWindows()) {
            systemSeparator = windowSeparator;
        }

        applicationPath = System.getProperty("user.dir");
        if (!StrUtil.endWith(applicationPath, systemSeparator)) {
            applicationPath += systemSeparator;
        }
    }

    public static boolean isWindows() {
        return FileUtil.isWindows();
    }

    /**
     * 通过注册地址获取保活地址
     */
    public static String convertKeepAliveUrl(final String registerUrl) {
        if (StrUtil.endWith(registerUrl, "register")) {
            return registerUrl.replace("register", "keep/alive");
        } else if (StrUtil.endWith(registerUrl, "register/")) {
            return registerUrl.replace("register/", "keep/alive");
        } else {
            return registerUrl;
        }
    }

    /**
     * 通过注册地址获取回调地址
     */
    public static String convertCallbackUrl(final String registerUrl) {
        if (StrUtil.endWith(registerUrl, "register")) {
            return registerUrl.replace("register", "callback");
        } else if (StrUtil.endWith(registerUrl, "register/")) {
            return registerUrl.replace("register/", "callback");
        } else {
            return registerUrl;
        }
    }

}

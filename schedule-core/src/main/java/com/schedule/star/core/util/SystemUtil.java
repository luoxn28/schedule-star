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

}

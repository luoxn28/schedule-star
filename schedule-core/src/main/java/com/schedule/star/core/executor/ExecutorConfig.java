package com.schedule.star.core.executor;

import cn.hutool.core.util.StrUtil;
import com.schedule.star.core.util.IDGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiangnan
 * @date 2018/3/4 19:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutorConfig {
    private String ip;
    private int    port;
    private String name;                // 执行器名字
    private String registerUrls;        // 注册地址，可能有多个，用 ',' 分隔
    private int    keepAliveTime = 10;  // 执行器与调度中心保活周期

    private String token = IDGenerator.getId();  // 可用于调度中心与执行器通信的安全验证

    // 配置文件路径
    private String configPath;

    public void printConfigItem(Logger logger) {
        if (logger == null) {
            return;
        }

        logger.info("执行器 ip: {}", ip);
        logger.info("执行器 port: {}", port);
        logger.info("执行器 name: {}", name);
        logger.info("执行器 registerUrl: {}", registerUrls);
        logger.info("心跳时间 keepAliveTime: {}", keepAliveTime);
        logger.info("执行器 token: {}", token);
    }

    public boolean valid() {
        try {
            return ipValid(ip) && (port > 0) && registerUrlsValid(registerUrls);
        } catch (Throwable e) {
            // not care e
            return false;
        }
    }

    private boolean ipValid(String ip) {
        if (StrUtil.isNotBlank(ip)) {
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." +
                            "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
                            "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
                            "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

            return ip.matches(regex);
        }

        return false;
    }

    private boolean registerUrlsValid(String registerUrls) {
        Boolean valid = false;
        if (StrUtil.isNotBlank(registerUrls)) {
            valid = true;
            List<String> urlList = new ArrayList<>(StrUtil.split(registerUrls, ','));
            for (String url : urlList) {
                if (!url.startsWith("http://")) {
                    valid = false;
                }
            }
        }

        return valid;
    }

}

package com.schedule.star.executor.config;

import cn.hutool.core.util.StrUtil;
import com.schedule.star.core.util.IDGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String registerUrls;

    private String token = IDGenerator.getId();
    private int    keepAliveTime = 10;  // 执行器与调度中心保活周期

    private String configPath;

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

package com.schedule.star.core.executor;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.schedule.star.core.bean.RegisterParam;
import com.schedule.star.core.rpc.jetty.JettyServer;
import com.schedule.star.core.thread.CallbackThread;
import com.schedule.star.core.thread.RegisterThread;
import com.schedule.star.core.util.IDGenerator;
import com.schedule.star.core.util.R;
import com.schedule.star.core.util.SystemUtil;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 执行器启动器
 *
 * @author xiangnan
 * @date 2018/1/30 22:37
 */
@Component
@Data
public class ExecutorStarter {
    private static Logger logger = LogManager.getLogger();

    // 执行器配置文件，所在目录为当前程序运行目录，注意配置文件中内容可能为中文，注意编码格式问题
    private static final String CONFIG_FILE_NAME = "executor.properties";
    public static final String CONFIG_FILE_PATH = SystemUtil.applicationPath + CONFIG_FILE_NAME;

    /**
     * 执行器配置信息
     *
     * TOKEN: 可用于调度中心与执行器通信的安全验证
     * REGISTER_URL: 注册URL
     * IP: 执行器IP
     * PORT: 执行器PORT
     * NAME: 执行器名字
     * KEEP_ALIVE_TIME: 保活时间
     */
    private String token = IDGenerator.getId();
    private String ip = "";
    private int    port = 9999;
    private String registerUrl = "";
    private String name = "";
    private int    keepAliveTime = 10;

    private List<String> registerUrlList = new ArrayList<>();  // 注册中心地址列表
    private List<String> keepAliveUrlList = new ArrayList<>(); // 保活地址列表
    private List<String> callbackUrlList = new ArrayList<>();  // 回调url地址列表，根据注册中心地址列表生成

    @Resource
    private JettyServer jettyServer;

    @Resource
    private RegisterThread registerThread;

    @Resource
    private CallbackThread callbackThread;

    public void run(ExecutorConfig config) {
        run(config.getIp(), config.getPort(), config.getRegisterUrls());
    }

    /**
     * @param ip ip
     * @param port port
     * @param registerUrl 注册地址，可能有多个，用 ',' 分隔
     */
    public void run(String ip, int port, String registerUrl) {
        this.registerUrl = registerUrl;

        run(ip, port, CollectionUtil.newArrayList(StrUtil.split(registerUrl, R.partFlag.registerUrl)));
    }

    private void run(String ip, int port, List<String> registerUrlList) {
        this.ip = ip;
        this.port = port;
        this.registerUrlList = registerUrlList;

        for (String registerUrl : registerUrlList) {
            this.keepAliveUrlList.add(SystemUtil.convertKeepAliveUrl(registerUrl));
            this.callbackUrlList.add(SystemUtil.convertCallbackUrl(registerUrl));
        }

        printConfigItem();

        try {
            RegisterParam registerParam = new RegisterParam(ip, port, token);
            registerParam.setKeepAliveTime(keepAliveTime);
            registerParam.setName(name);

            registerThread.start(registerUrlList, keepAliveUrlList, registerParam);
            callbackThread.start(callbackUrlList);
            if (jettyServer.start(ip, port)) {
                // 主线程join等待
                jettyServer.join();
            }

        } catch (Exception e) {
            logger.warn("执行器运行异常: {}", e);
        } finally {
            destroy();
        }
    }

    private void destroy() {
        if (jettyServer != null) {
            jettyServer.destroy();
        }

        if (registerThread != null) {
            registerThread.stop();
        }

        if (callbackThread != null) {
            callbackThread.stop();
        }
    }

    private void printConfigItem() {
        logger.info("执行器 token: {}", token);
        logger.info("执行器 ip: {}", ip);
        logger.info("执行器 port: {}", port);
        logger.info("执行器 registerUrl: {}", registerUrlList);
        logger.info("执行器 name: {}", name);
        logger.info("保活时间 keepAliveTime: {}", keepAliveTime);
    }
}

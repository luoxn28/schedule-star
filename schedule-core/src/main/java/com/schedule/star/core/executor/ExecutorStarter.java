package com.schedule.star.core.executor;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.schedule.star.core.bean.RegisterParam;
import com.schedule.star.core.rpc.jetty.JettyServer;
import com.schedule.star.core.thread.CallbackThread;
import com.schedule.star.core.thread.RegisterThread;
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

    private ExecutorConfig config;

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

        if ((config == null) || !config.valid()) {
            logger.info("启动器配置参数错误： {}", config);
            System.exit(-1);
        }

        this.config = config;
        start();
    }

    private void start() {
        this.registerUrlList = CollectionUtil.newArrayList(StrUtil.split(config.getRegisterUrls(), R.partFlag.registerUrl));
        for (String registerUrl : registerUrlList) {
            this.keepAliveUrlList.add(SystemUtil.convertKeepAliveUrl(registerUrl));
            this.callbackUrlList.add(SystemUtil.convertCallbackUrl(registerUrl));
        }

        config.printConfigItem(logger);

        try {
            RegisterParam registerParam = new RegisterParam(config.getIp(), config.getPort(), config.getToken());
            registerParam.setKeepAliveTime(config.getKeepAliveTime());
            registerParam.setName(config.getName());

            registerThread.start(registerUrlList, keepAliveUrlList, registerParam);
            callbackThread.start(callbackUrlList);
            if (jettyServer.start(config.getIp(), config.getPort())) {
                // 主线程join等待
                jettyServer.join();
            }

        } catch (Exception e) {
            logger.warn("执行器运行异常: {}", e);
        } finally {
            destroy();
        }
    }

    public int getKeepAliveTime() {
        return config != null ? config.getKeepAliveTime() : 10;
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

}

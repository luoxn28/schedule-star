package com.schedule.star.core.executor;

import com.schedule.star.core.rpc.jetty.JettyServer;
import com.schedule.star.core.thread.RegisterThread;
import com.schedule.star.core.util.IDGenerator;
import com.schedule.star.core.util.SystemUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 执行器启动器
 *
 * @author xiangnan
 * @date 2018/1/30 22:37
 */
@Component
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
    public static final String TOKEN = IDGenerator.getId();
    public static String REGISTER_URL = "";
    public static String IP = "";
    public static int PORT = 9999;
    public static String NAME = "";
    public static int KEEP_ALIVE_TIME = 10;

    @Resource
    private JettyServer jettyServer;

    @Resource
    private RegisterThread registerThread;
}

package com.schedule.star.core.rpc.jetty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author xiangnan
 * @date 2018/1/30 22:27
 */
@Component
public class JettyServer {
    private final static Logger logger = LogManager.getLogger();

    private Server jettyServer;

    public boolean start(String ip, int port) {
        // 自定义线程池，供jetty使用
        ExecutorThreadPool threadPool = new ExecutorThreadPool(
                32, 256, 60L, TimeUnit.SECONDS);
        jettyServer = new Server(threadPool);
        ServerConnector connector = new ServerConnector(jettyServer);
        connector.setHost(ip);
        connector.setPort(port);
        jettyServer.setConnectors(new Connector[]{ connector });

        // jetty handler
        HandlerCollection handlers =new HandlerCollection();
        handlers.setHandlers(new Handler[] { new JettyServerHandler() });
        jettyServer.setHandler(handlers);

        try {
            jettyServer.start();

            logger.info("jetty 启动成功啦...");
        } catch (Exception e) {
            logger.error("Jetty服务启动失败: " + e);
            return false;
        }

        return true;
    }

    public void join() throws InterruptedException {
        if (jettyServer != null) {
            jettyServer.join();
        }
    }

    public void destroy() {
        if (jettyServer != null) {
            try {
                jettyServer.stop();
                jettyServer.destroy();
                logger.error("Jetty服务器停止success");
            } catch (Exception e) {
                logger.error("Jetty服务器停止失败: " + e);
            }
        }
    }

}

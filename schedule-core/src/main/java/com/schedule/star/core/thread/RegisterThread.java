package com.schedule.star.core.thread;

import cn.hutool.core.util.StrUtil;
import com.schedule.star.core.bean.RegisterParam;
import com.schedule.star.core.bean.Result;
import com.schedule.star.core.executor.ExecutorStarter;
import com.schedule.star.core.util.HttpUtil;
import com.schedule.star.core.util.SystemUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xiangnan
 * @date 2018/1/30 22:39
 */
@Component
public class RegisterThread {
    private static final Logger logger = LogManager.getLogger();

    @Resource
    private ExecutorStarter executorStarter;

    @Resource
    private CallbackThread callbackThread;

    private boolean running = true;

    public void start(final List<String> registerUrlList, final List<String> keepAliveUrlList, final RegisterParam param) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("RegisterThread start success");

                List<String> onlineRegisterUrlList = new ArrayList<>(registerUrlList);
                List<String> onlineKeepAliveUrlList = new ArrayList<>(keepAliveUrlList);

                // 是否处于心跳状态
                boolean keepAlive = false;

                while (running) {
                    try {
                        if (keepAlive) {
                            for (String keepAliveUrl : onlineKeepAliveUrlList) {
                                Result result = HttpUtil.keepAlive(keepAliveUrl, param.getIp(), param.getPort());
                                if (result.isSuccess()) {
                                    keepAlive = true;
                                    break;
                                } else {
                                    logger.warn("执行器保活失败, {} {}，准备重新进行注册", keepAliveUrl, result.getData());
                                    keepAlive = false;
                                }
                            }
                        } else {
                            // 将要临时被移除的注册中心
                            String willBeRemoveUrl = null;

                            for (String registerUrl: onlineRegisterUrlList) {
                                logger.info("执行器准备注册，注册url: {}", registerUrl);

                                Result result = HttpUtil.register(registerUrl, param);
                                if (result.isSuccess()) {
                                    // 注册成功后开始心跳
                                    keepAlive = true;

                                    logger.info("执行器注册成功，注册url: {}", registerUrl);
                                    logger.info("执行器与调度中心保活中...");

                                    // 注册成功就不往下一个调度中心注册了
                                    break;
                                } else {
                                    logger.warn("执行器注册失败，{} result: {}", registerUrl, result.getData());
                                    // 注册失败会往下一个调度中心注册
                                    willBeRemoveUrl = registerUrl;
                                    break;
                                }
                            }

                            if (StrUtil.isNotEmpty(willBeRemoveUrl)) {
                                logger.warn("临时移除注册中心: {}", willBeRemoveUrl);

                                onlineRegisterUrlList.remove(willBeRemoveUrl);
                                onlineKeepAliveUrlList.remove(SystemUtil.convertKeepAliveUrl(willBeRemoveUrl));

                                // 从回调线程中移除该回调url
                                callbackThread.removeCallbackUrl(SystemUtil.convertCallbackUrl(willBeRemoveUrl));
                            }
                        }

                        // 如果两个urlList大小不一致，则表示有的调度中心挂了
                        if (onlineRegisterUrlList.size() == registerUrlList.size()) {
                            TimeUnit.SECONDS.sleep(executorStarter.getKeepAliveTime());
                        } else {
                            if (onlineRegisterUrlList.isEmpty()) {
                                onlineRegisterUrlList = new ArrayList<>(registerUrlList);
                                onlineKeepAliveUrlList = new ArrayList<>(keepAliveUrlList);

                                callbackThread.updateCallbackUrlList(new ArrayList<>(executorStarter.getCallbackUrlList()));
                                TimeUnit.SECONDS.sleep(2);
                            }
                        }

                    } catch (Exception e) {
                        logger.error("注册线程出现异常 {}", e.getMessage());
                    }

                }

            }
        }, "RegisterThread");
        thread.start();
    }

    public void stop() {
        this.running = false;
    }
}

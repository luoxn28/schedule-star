package com.schedule.star.core.thread;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.schedule.star.core.bean.CallbackParam;
import com.schedule.star.core.bean.Result;
import com.schedule.star.core.util.HttpUtil;
import com.schedule.star.core.util.R;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author xiangnan
 * @date 2018/3/10 7:19
 */
@Component
public class CallbackThread {
    private static final Logger logger = LogManager.getLogger();

    private static LinkedBlockingQueue<CallbackParam> callBackQueue = new LinkedBlockingQueue<CallbackParam>();

    private boolean running = true;
    private volatile String willBeRemoveUrl = null;
    private volatile List<String> updateCallbackUrlList = null;

    public static boolean pushCallback(CallbackParam callbackParam) {
        return callbackParam == null || callBackQueue.offer(callbackParam);
    }

    public void start(final List<String> callbackUrlList) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("CallbackThread start success");

                List<String> onlineCallbackUrlList = new ArrayList<>(callbackUrlList);

                while (running) {

                    if (StrUtil.isNotBlank(willBeRemoveUrl)) {
                        onlineCallbackUrlList.remove(willBeRemoveUrl);
                        willBeRemoveUrl = null;
                    }

                    if (CollectionUtil.isNotEmpty(updateCallbackUrlList)) {
                        onlineCallbackUrlList = updateCallbackUrlList;
                        updateCallbackUrlList = null;
                    }

                    if (onlineCallbackUrlList.isEmpty()) {
                        try {
                            // 竟然没有回调的调度中心了，休息1s，然后再起来工作吧
                            TimeUnit.SECONDS.sleep(1);
                            continue;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    CallbackParam callbackParam = null;
                    try {
                        callbackParam = callBackQueue.take();
                        if (callbackParam != null) {
                            Result result = Result.FAIL;
                            for (String url : onlineCallbackUrlList) {
                                result = HttpUtil.callback(url, callbackParam);

                                logger.info("callback {}:{} to {} {}",
                                        callbackParam.getJobName(), callbackParam.getLogId(), url, result.getStatus());
                                if (result.isSuccess() && R.callback.CAN_DELETE.equals(result.getData().toString())) {
                                    break;
                                }
                            }

                            if (!result.isSuccess()) {
                                // 发送给所有的调度中心都没有成功，那就等会再发送callback消息
                                callBackQueue.offer(callbackParam);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("回调线程异常，callbackUrl {}, e: {}", onlineCallbackUrlList, e);
                        if (callbackParam != null) {
                            callBackQueue.offer(callbackParam);
                        }
                    }
                }
            }
        }, "CallbackThread");
        thread.start();
    }

    public void removeCallbackUrl(String callbackUrl) {
        willBeRemoveUrl = callbackUrl;
    }

    public void updateCallbackUrlList(List<String> callbackUrlList) {
        updateCallbackUrlList = callbackUrlList;
    }

    public void stop() {
        this.running = false;
    }
}

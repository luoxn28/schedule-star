package com.schedule.star.core.rpc.handler;

import com.schedule.star.core.bean.CallbackParam;
import com.schedule.star.core.bean.Result;
import com.schedule.star.core.rpc.bean.TriggerParam;
import com.schedule.star.core.thread.CallbackThread;
import com.schedule.star.core.util.R;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * @author xiangnan
 * @date 2018/3/10 12:34
 */
@AllArgsConstructor
public class JobTask implements Runnable {
    private static final Logger logger = LogManager.getLogger();

    private JobHandler jobHandler;
    private TriggerParam triggerParam;

    @Override
    public void run() {
        CallbackParam callbackParam = new CallbackParam(
                triggerParam.getJobId(), triggerParam.getJobName(), triggerParam.getLogId());
        callbackParam.setHandleTime(new Date());
        callbackParam.setChildJobs(triggerParam.getChildJobs());

        logger.debug("start run job:{} jobId: {}, logId: {}",
                triggerParam.getJobName(), triggerParam.getJobId(), triggerParam.getLogId());

        Result result = Result.success(null);
        try {
            // 任务执行
            result = jobHandler.execute(triggerParam);
        } catch (Exception e) {
            logger.error("任务执行时发生了异常，e: {}", e);
            e.printStackTrace();

            result.setStatus(R.status.FAIL);
            result.setData(e.toString());
        }

        callbackParam.setFinishTime(new Date());
        callbackParam.setHandleResult(result.getStatus());
        callbackParam.setHandleContent(result.getData().toString());

        if (!CallbackThread.pushCallback(callbackParam)) {
            // callback队列已满
            logger.error("callback队列竟然满了。。。。。");
        }
    }

}

package com.schedule.star.core.rpc.service.impl;

import com.schedule.star.core.bean.Result;
import com.schedule.star.core.rpc.bean.TriggerParam;
import com.schedule.star.core.rpc.handler.JobTask;
import com.schedule.star.core.rpc.handler.impl.JavaJobHandler;
import com.schedule.star.core.rpc.handler.impl.ScriptJobHandler;
import com.schedule.star.core.rpc.service.TriggerService;
import com.schedule.star.core.util.R;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xiangnan
 * @date 2018/1/29 17:56
 */
public class TriggerServiceImpl implements TriggerService {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Result run(TriggerParam param) {
        if (param == null) {
            return Result.fail("param is null");
        }

        Result result = Result.success(null);
        switch (param.getJobType()) {
            case R.jobType.Java: {
                JobFactory.execute(new JobTask(new JavaJobHandler(), param));
                break;
            }
            case R.jobType.Python:
            case R.jobType.Shell: {
                JobFactory.execute(new JobTask(new ScriptJobHandler(), param));
                break;
            }
            default: {
                result.setStatus(R.status.FAIL);
                result.setData("jobType error");
            }
        }

        logger.info("receive job, jobId: {}:{}, logId: {}", param.getJobName(), param.getJobId(), param.getLogId());
        return result;
    }

    static class JobFactory {

        private static ExecutorService executor= new ThreadPoolExecutor(
                8, 16, 60L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());

        static void execute(Runnable command) {
            executor.execute(command);
        }

    }

}

package com.schedule.star.core.rpc.handler.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.schedule.star.core.bean.Result;
import com.schedule.star.core.rpc.bean.TriggerParam;
import com.schedule.star.core.rpc.handler.JobHandler;
import com.schedule.star.core.util.R;
import com.schedule.star.core.util.SystemUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author xiangnan
 * @date 2018/3/10 12:45
 */
@Data
@AllArgsConstructor
public class ScriptJobHandler implements JobHandler {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Result execute(TriggerParam triggerParam) {

        String cmd, suffix;

        switch (triggerParam.getJobType()) {
            case R.jobType.Shell: {
                cmd = "bash";
                suffix = ".sh";
                break;
            }
            case R.jobType.Python: {
                cmd = "python";
                suffix = ".py";
                break;
            }
            default: {
                return Result.fail("不支持的文件类型");
            }
        }

        Result result = Result.success(null);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayOutputStream err = new ByteArrayOutputStream();
            File file = FileUtil.writeBytes(triggerParam.getScriptSource().getBytes(),
                    SystemUtil.applicationPath + "scripts/" + triggerParam.getJobId() + suffix);

            int exit = cmdExecute(triggerParam.getLogId(), cmd, file.getAbsolutePath(),
                    out, err, triggerParam.getParams());
            if (exit == 0) {
                result.setData(out.toString());
            } else {
                // 脚本执行失败
                result.setStatus(R.status.FAIL);
                result.setData(StrUtil.format("exit: {}, out: {}, err: {}",
                        exit, out.toString(), err.toString()));
            }

        } catch (Exception e) {
            logger.error("执行脚本出错：{}", e.toString());

            result.setStatus(R.status.FAIL);
            result.setData(e.toString());
        }

        return result;
    }

    private int cmdExecute(String logId, String cmd, String filename,
                           OutputStream out, OutputStream err, String... params) throws IOException {

        CommandLine commandline = new CommandLine(cmd);
        commandline.addArgument(filename);
        if (ArrayUtil.isNotEmpty(params)) {
            commandline.addArguments(params);
        }

        DefaultExecutor exec = new DefaultExecutor();
        exec.setExitValues(null);
        exec.setStreamHandler(new PumpStreamHandler(out, err, null));

        // 设置watchdog
        final ExecuteWatchdog watchdog = new ExecuteWatchdog(Integer.MAX_VALUE);
        exec.setWatchdog(watchdog);

        try {
            WatchdogComponent.add(logId, watchdog);
            return exec.execute(commandline);
        } catch (Exception e) {
            throw e;
        } finally {
            // 脚本进程执行完毕，删除该watchdog
            WatchdogComponent.del(logId);
        }
    }

    static class WatchdogComponent {

        // 类属性 <logId, ExecuteWatchdog>
        private static ConcurrentMap<String, ExecuteWatchdog> dogMap = new ConcurrentHashMap<>();

        static ExecuteWatchdog get(String logId) {
            return dogMap.get(logId);
        }

        static void add(String logId, ExecuteWatchdog watchdog) {
            dogMap.putIfAbsent(logId, watchdog);
        }

        static void del(String logId) {
            dogMap.remove(logId);
        }
    }

}

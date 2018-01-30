package com.schedule.star.core.rpc.service;

import com.schedule.star.core.bean.Result;
import com.schedule.star.core.rpc.bean.ExecutorInfo;

/**
 * 执行器配置服务类，用于调度中心在线修改执行器信息
 *
 * @author xiangnan
 * @date 2018/1/30 22:21
 */
public interface ExecutorConfigService {

    Result updateExecutorConfig(ExecutorInfo executorInfo);

}

package com.schedule.star.core.rpc.service;

import com.schedule.star.core.bean.Result;
import com.schedule.star.core.rpc.bean.TriggerParam;

/**
 * 任务执行 rpc服务
 *
 * @author xiangnan
 * @date 2018/1/29 17:48
 */
public interface TriggerService {

    Result run(TriggerParam param);

}

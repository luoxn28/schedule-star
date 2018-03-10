package com.schedule.star.core.rpc.handler;

import com.schedule.star.core.bean.Result;
import com.schedule.star.core.rpc.bean.TriggerParam;

/**
 * @author xiangnan
 * @date 2018/3/10 12:35
 */
public interface JobHandler {

    Result execute(TriggerParam param);

}

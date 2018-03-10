package com.schedule.star.core.rpc.handler.impl;

import com.schedule.star.core.bean.Result;
import com.schedule.star.core.rpc.bean.TriggerParam;
import com.schedule.star.core.rpc.handler.JobHandler;
import lombok.Data;

/**
 * @author xiangnan
 * @date 2018/3/10 12:45
 */
@Data
public class JavaJobHandler implements JobHandler {

    @Override
    public Result execute(TriggerParam param) {
        return null;
    }

}

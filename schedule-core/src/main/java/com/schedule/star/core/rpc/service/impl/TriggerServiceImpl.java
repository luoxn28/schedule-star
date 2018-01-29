package com.schedule.star.core.rpc.service.impl;

import com.schedule.star.core.bean.Result;
import com.schedule.star.core.rpc.bean.TriggerParam;
import com.schedule.star.core.rpc.service.TriggerService;

/**
 * @author xiangnan
 * @date 2018/1/29 17:56
 */
public class TriggerServiceImpl implements TriggerService {

    @Override
    public Result run(TriggerParam param) {

        System.out.println(param);

        return new Result();
    }

}

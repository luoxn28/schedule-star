package com.schedule.star.admin.component.router.impl;

import com.schedule.star.admin.component.router.ExecutorRouter;
import com.schedule.star.admin.entity.ExecutorEntity;

import java.util.List;

/**
 * @author xiangnan
 * @date 2018/3/18 14:41
 */
public class FirstExecutorRouter extends ExecutorRouter {

    @Override
    protected ExecutorEntity routerInternal(List<ExecutorEntity> executorList) {
        return executorList.get(0);
    }

}

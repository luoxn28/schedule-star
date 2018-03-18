package com.schedule.star.admin.component.router.impl;

import com.schedule.star.admin.component.router.ExecutorRouter;
import com.schedule.star.admin.entity.ExecutorEntity;

import java.util.List;
import java.util.Random;

/**
 * @author xiangnan
 * @date 2018/3/18 14:39
 */
public class RandomExecutorRouter extends ExecutorRouter {

    @Override
    protected ExecutorEntity routerInternal(List<ExecutorEntity> executorList) {
        return executorList.get(new Random().nextInt(executorList.size()));
    }

}

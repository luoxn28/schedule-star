package com.schedule.star.admin.component.router;

import cn.hutool.core.collection.CollectionUtil;
import com.schedule.star.admin.entity.ExecutorEntity;

import java.util.List;

/**
 * @author xiangnan
 * @date 2018/3/18 14:35
 */
public abstract class ExecutorRouter {

    public ExecutorEntity router(List<ExecutorEntity> executorList) {
        return CollectionUtil.isEmpty(executorList) ? null : routerInternal(executorList);
    }

    protected abstract ExecutorEntity routerInternal(List<ExecutorEntity> executorList);
}

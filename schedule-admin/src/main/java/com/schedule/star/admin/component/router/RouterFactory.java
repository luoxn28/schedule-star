package com.schedule.star.admin.component.router;

import cn.hutool.core.util.StrUtil;
import com.schedule.star.admin.component.router.impl.FirstExecutorRouter;
import com.schedule.star.admin.component.router.impl.RandomExecutorRouter;
import com.schedule.star.core.util.R;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiangnan
 * @date 2018/3/18 14:34
 */
@Component
public class RouterFactory {

    private static ConcurrentHashMap<String, ExecutorRouter> routerMap = new ConcurrentHashMap<>();

    static {
        // init
        routerMap.put(R.routerStrategy.RANDOM, new RandomExecutorRouter());
        routerMap.put(R.routerStrategy.FIRST, new FirstExecutorRouter());
    }

    public ExecutorRouter get(String routerStrategy) {
        if (StrUtil.isBlank(routerStrategy)) {
            return routerMap.get(R.routerStrategy.RANDOM);
        } else {
            ExecutorRouter router = routerMap.get(routerStrategy);
            return router != null ? router : routerMap.get(R.routerStrategy.RANDOM);
        }
    }

}

package com.schedule.star.core.rpc;

import com.schedule.star.core.rpc.encoder.RpcRequest;
import com.schedule.star.core.rpc.encoder.RpcResponse;
import com.schedule.star.core.rpc.service.TriggerService;
import com.schedule.star.core.rpc.service.impl.TriggerServiceImpl;
import com.schedule.star.core.util.R;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * rpc 服务工厂类
 *
 * @author xiangnan
 * @date 2018/1/29 17:14
 */
public class RpcServiceFactory {
    private static final Logger logger = LogManager.getLogger();

    private static ConcurrentMap<String, Class> serviceMap = new ConcurrentHashMap<>();

    // 服务注册
    static {
        registerService(TriggerService.class, TriggerServiceImpl.class);

        logger.info("rpc service map: " + serviceMap);
    }

    public static void registerService(Class clazz, Class clazzImpl) {
        serviceMap.put(clazz.getName(), clazzImpl);
    }

    public static RpcResponse invoke(RpcRequest request) {
        Class clazz = serviceMap.get(request.getClazz());
        if (clazz == null) {
            return new RpcResponse(R.status.FAIL, "呜呜，没找到对应的服务类...", null);
        }

        RpcResponse response = new RpcResponse();
        try {
            FastClass serviceClass = FastClass.create(clazz);
            FastMethod serviceMethod = serviceClass.getMethod(request.getMethod(), request.getParamType());

            Object result = serviceMethod.invoke(clazz.newInstance(), request.getParam());
            response.setResult(result);
        } catch (Exception e) {
            logger.error("执行服务时发生异常，e: ", e);
            response.setStatusMessage(R.status.FAIL, e.getMessage());
        }

        return response;
    }

}

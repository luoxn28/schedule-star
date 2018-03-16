package com.schedule.star.core.rpc;

import cn.hutool.core.util.ArrayUtil;
import com.schedule.star.core.rpc.encoder.RpcRequest;
import com.schedule.star.core.rpc.encoder.RpcResponse;
import com.schedule.star.core.util.HessianUtil;
import com.schedule.star.core.util.HttpUtil;
import com.schedule.star.core.util.R;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author xiangnan
 * @date 2018/1/29 19:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcClientProxy implements FactoryBean<Object> {
    private final static Logger logger = LogManager.getLogger();

    private Class<?> clazz;
    private String   toUrl;

    @Override
    public Object getObject() throws Exception {

        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                RpcRequest request = new RpcRequest();
                request.setToUrl(toUrl);
                request.setClazz(method.getDeclaringClass().getName());
                request.setMethod(method.getName());
                request.setParamType(method.getParameterTypes());
                request.setParam(args);

                RpcResponse response =  send(request);
                if (response.success()) {
                    return response.getResult();
                } else {
                    throw new RuntimeException(response.getMessage());
                }
            }
        });

    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    private RpcResponse send(RpcRequest request) {

        RpcResponse rpcResponse = new RpcResponse();

        try {
            byte[] response = HttpUtil.postRpc(request.getToUrl(), HessianUtil.serialize(request));
            if (!ArrayUtil.isEmpty(response)) {
                rpcResponse = HessianUtil.deserialize(response);
            } else {
                rpcResponse.setStatusMessage(R.status.FAIL, "RpcResponse byte[] is null");
            }
        } catch (Exception e) {
            logger.error("rpc调用异常，e: ", e);
            rpcResponse.setStatusMessage(R.status.FAIL, e.getMessage());
        }

        return rpcResponse;
    }

}

package com.schedule.star.core.rpc.encoder;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * rpc请求封装类
 *
 * @author xiangnan
 * @date 2018/1/29 17:25
 */
@Data
@NoArgsConstructor
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1973794251742911519L;

    private String fromUrl;
    private String toUrl;
    private String clazz;          // RPC调用类
    private String method;         // RPC调用类方法
    private Class<?>[] paramType;  // 参数类型
    private Object[] param;        // 参数
}

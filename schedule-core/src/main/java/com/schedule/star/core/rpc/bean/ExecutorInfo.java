package com.schedule.star.core.rpc.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 执行器信息类
 *
 * @author xiangnan
 * @date 2018/1/30 22:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutorInfo implements Serializable {
    private static final long serialVersionUID = 1267215630950460331L;

    private String ip;
    private int port;
    private String token;
    private String name;
    private int keepAliveTime;
}

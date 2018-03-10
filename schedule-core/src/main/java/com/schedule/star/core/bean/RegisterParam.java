package com.schedule.star.core.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xiangnan
 * @date 2018/3/10 7:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterParam implements Serializable {
    private String ip;
    private int port;
    private String token;
    private String name; // 执行器名字
    private int keepAliveTime;

    public RegisterParam(String ip, int port, String token) {
        this.ip = ip;
        this.port = port;
        this.token = token;
    }
}

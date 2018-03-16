package com.schedule.star.admin.entity;

import com.schedule.star.core.bean.RegisterParam;
import com.schedule.star.core.util.IDGenerator;
import com.schedule.star.core.util.R;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xiangnan
 * @date 2018/1/29 18:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutorEntity {
    private int id;
    private String executorId;
    private String ip;
    private int port;
    private String name;
    private String token;
    private String status;         // 执行器当前状态，见 R.executorStatus
    private int keepAliveTime;     // 与调度中心的保活时间（单位s）
    private Date registerTime;
    private Date updateTime;

    public ExecutorEntity(String executorId, String status) {
        this.executorId = executorId;
        this.status = status;
    }

    public ExecutorEntity(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static ExecutorEntity build(RegisterParam param) {
        if (param == null) {
            return null;
        }

        ExecutorEntity entity = new ExecutorEntity();
        entity.setExecutorId(IDGenerator.getId());
        entity.setRegisterTime(new Date());
        entity.setStatus(R.executorStatus.ONLINE);
        entity.setIp(param.getIp());
        entity.setPort(param.getPort());
        entity.setName(param.getName());
        entity.setToken(param.getToken());
        entity.setKeepAliveTime(param.getKeepAliveTime());

        return entity;
    }

}

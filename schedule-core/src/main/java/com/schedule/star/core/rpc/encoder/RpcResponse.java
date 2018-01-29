package com.schedule.star.core.rpc.encoder;

import cn.hutool.core.util.StrUtil;
import com.schedule.star.core.util.R;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * rpc响应封装类
 *
 * @author xiangnan
 * @date 2018/1/29 17:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse implements Serializable {
    private static final long serialVersionUID = -2485825387298187462L;

    private String status;
    private String message; // some detail info
    private Object result;

    public boolean success() {
        return (status == null) || StrUtil.equals(status, R.status.SUCCESS);
    }

    public void setStatusMessage(String status, String message) {
        this.status = status;
        this.message = message;
    }
}

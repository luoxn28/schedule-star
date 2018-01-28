package com.schedule.star.core.bean;

import cn.hutool.core.util.StrUtil;
import com.schedule.star.core.util.R;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通信结果类
 *
 * @author xiangnan
 * @date 2018/1/28 15:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result <T> implements Serializable {
    private static final long serialVersionUID = 2230572083465237113L;

    private String status;
    private T data;

    public static Result<String> SUCCESS = new Result<>(R.status.SUCCESS, null);
    public static Result<String> FAIL = new Result<>(R.status.FAIL, null);

    public boolean success() {
        return StrUtil.equals(R.status.SUCCESS, status);
    }

}

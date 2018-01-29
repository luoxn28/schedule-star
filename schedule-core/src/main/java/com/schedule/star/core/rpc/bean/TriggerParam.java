package com.schedule.star.core.rpc.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xiangnan
 * @date 2018/1/29 17:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TriggerParam implements Serializable {
    private static final long serialVersionUID = 7383349703914074460L;

    private String logId;
    private String jobId;
}

package com.schedule.star.admin.service;

import com.schedule.star.core.bean.CallbackParam;
import com.schedule.star.core.bean.RegisterParam;
import com.schedule.star.core.bean.Result;

/**
 * 执行器通信API服务类
 *
 * @author xiangnan
 * @date 2018/3/10 10:01
 */
public interface ApiService {

    Result register(RegisterParam param);

    Result keepAlive(String ip, int port);

    /**
     * 任务执行信息回调
     *
     * @return result，当返回信息result.status为SUCCESS，并且result.msg为CAN_DELETED时，
     * 执行器可将该次回调信息删除，否则应该一直发送该次回调信息给调度中心
     */
    Result callback(CallbackParam callbackParam);

}

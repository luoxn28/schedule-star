package com.schedule.star.admin.service.impl;

import com.schedule.star.admin.dao.ExecutorDao;
import com.schedule.star.admin.entity.ExecutorEntity;
import com.schedule.star.admin.service.ApiService;
import com.schedule.star.core.bean.CallbackParam;
import com.schedule.star.core.bean.RegisterParam;
import com.schedule.star.core.bean.Result;
import com.schedule.star.core.util.R;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xiangnan
 * @date 2018/3/10 10:02
 */
@Service
public class ApiServiceImpl implements ApiService {
    private static final Logger logger = LogManager.getLogger();

    @Resource
    private ExecutorDao executorDao;

    @Override
    public Result register(RegisterParam param) {
        ExecutorEntity entity = ExecutorEntity.build(param);
        if (entity == null) {
            return null;
        }

        return executorDao.updateByIpPort(entity) > 0 ? Result.SUCCESS : Result.FAIL;
    }

    @Override
    public Result keepAlive(String ip, int port) {

        ExecutorEntity entity = new ExecutorEntity(ip, port);
        entity.setStatus(R.executorStatus.ONLINE);

        return executorDao.updateByIpPort(entity) > 0 ? Result.SUCCESS : Result.FAIL;
    }

    @Override
    public Result callback(CallbackParam callbackParam) {
        System.out.println("callback还没开始写呢");
        return null;
    }

}

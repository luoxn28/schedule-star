package com.schedule.star.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.schedule.star.admin.bean.ExecutorBean;
import com.schedule.star.admin.dao.ExecutorDao;
import com.schedule.star.admin.entity.ExecutorEntity;
import com.schedule.star.admin.service.ExecutorService;
import com.schedule.star.core.bean.Result;
import com.schedule.star.core.rpc.RpcClientProxy;
import com.schedule.star.core.rpc.bean.ExecutorInfo;
import com.schedule.star.core.rpc.service.ExecutorConfigService;
import com.schedule.star.core.util.R;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.schedule.star.admin.service.impl.ExecutorServiceImpl.ExecutorConvert.toBeanList;
import static com.schedule.star.admin.service.impl.ExecutorServiceImpl.ExecutorConvert.toEntity;

/**
 * @author xiangnan
 * @date 2018/1/31 22:19
 */
@Service
public class ExecutorServiceImpl implements ExecutorService {
    private final static Logger logger = LogManager.getLogger();

    @Resource
    private ExecutorDao executorDao;

    @Override
    public List<ExecutorBean> getExecutorList() {
        return toBeanList(executorDao.selectList());
    }

    @Override
    @Transactional
    public int updateExecutor(ExecutorBean bean) {
        ExecutorEntity entity = toEntity(bean);
        ExecutorEntity oldEntity = executorDao.selectByExecutorId(bean.getExecutorId());
        if ((oldEntity == null) || !StrUtil.equals(oldEntity.getStatus(), R.executorStatus.ONLINE)) {
            throw new RuntimeException("未找到执行器信息或者执行器不在线");
        }

        if (executorDao.updateByExecutorId(entity) != 1) {
            logger.warn("更新执行器信息时失败，bean: " + bean);
            throw new RuntimeException("更新执行器信息时失败");
        }

        logger.info("更新执行器成功: executorId=" + entity.getExecutorId()
                + ", ip=" + entity.getIp() + ", port=" + entity.getPort());

        // 在线通知执行器更改
        Result result = onlineUpdateExecutorInfo(executorDao.selectByExecutorId(bean.getExecutorId()));
        if (!result.success()) {
            logger.warn("在线通知执行器更改失败，bean: " + result.getData());
            throw new RuntimeException("在线通知执行器更改失败 " + result.getData());
        }

        return 1;
    }

    @Override
    public int deleteExecutor(String executorId) {
        if (1 != executorDao.updateByExecutorId(new ExecutorEntity(executorId, R.executorStatus.DELETED))) {
            logger.info("删除执行器失败: executorId=" + executorId);
            throw new RuntimeException("删除执行器失败");
        }

        logger.info("删除执行器成功: executorId=" + executorId);
        return 1;
    }

    private Result onlineUpdateExecutorInfo(ExecutorEntity entity) {

        ExecutorInfo executorInfo = new ExecutorInfo();
        executorInfo.setIp(entity.getIp());
        executorInfo.setPort(entity.getPort());
        executorInfo.setToken(entity.getToken());
        executorInfo.setName(entity.getName());
        executorInfo.setKeepAliveTime(entity.getKeepAliveTime());

        try {
            String toUrl = "http://" + entity.getIp() + ":" + entity.getPort();
            ExecutorConfigService configService = (ExecutorConfigService)
                    new RpcClientProxy(ExecutorConfigService.class, toUrl).getObject();

            return configService.updateExecutorConfig(executorInfo);
        } catch (Exception e) {
            logger.error("在线更改执行器信息错误, e:", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * Bean转换类
     */
    static class ExecutorConvert {

        static ExecutorBean toBean(ExecutorEntity entity) {
            ExecutorBean bean = null;

            if (entity != null) {
                bean = new ExecutorBean();
                BeanUtil.copyProperties(entity, bean);
            }
            return bean;
        }

        static ExecutorEntity toEntity(ExecutorBean bean) {
            ExecutorEntity entity = null;

            if (bean != null) {
                entity = new ExecutorEntity();
                BeanUtil.copyProperties(bean, entity);
            }
            return entity;
        }

        static List<ExecutorBean> toBeanList(List<ExecutorEntity> entityList) {
            List<ExecutorBean> beanList = new ArrayList<>();
            if (entityList != null) {
                for (ExecutorEntity entity : entityList) {
                    beanList.add(toBean(entity));
                }
            }

            return beanList;
        }

    }

}

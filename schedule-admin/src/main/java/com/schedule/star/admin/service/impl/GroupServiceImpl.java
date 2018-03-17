package com.schedule.star.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.schedule.star.admin.bean.GroupBean;
import com.schedule.star.admin.dao.ExecutorDao;
import com.schedule.star.admin.dao.GroupDao;
import com.schedule.star.admin.entity.ExecutorEntity;
import com.schedule.star.admin.entity.GroupEntity;
import com.schedule.star.admin.service.GroupService;
import com.schedule.star.core.util.IDGenerator;
import com.schedule.star.core.util.R;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.schedule.star.admin.service.impl.GroupServiceImpl.GroupConvert.toBean;
import static com.schedule.star.admin.service.impl.GroupServiceImpl.GroupConvert.toEntity;

/**
 * @author xiangnan
 * @date 2018/3/17 11:30
 */
@Service
public class GroupServiceImpl implements GroupService {
    private static final Logger logger = LogManager.getLogger();

    @Resource
    private GroupDao groupDao;

    @Resource
    private ExecutorDao executorDao;

    @Override
    public List<GroupBean> getGroupList() {
        List<GroupEntity> entityList = groupDao.selectList();
        List<GroupBean> beanList = new ArrayList<>();

        for (GroupEntity entity : entityList) {
            List<ExecutorEntity> tmpList = new ArrayList<>();
            List<String> executorIdList = groupDao.selectExecutorIdByGroupId(entity.getGroupId());
            for (String executorId : executorIdList) {
                tmpList.add(executorDao.selectByExecutorId(executorId));
            }

            GroupBean bean = toBean(entity);
            if (bean != null) {
                bean.setExecutorList(ExecutorServiceImpl.ExecutorConvert.toBeanList(tmpList));
            }

            beanList.add(bean);
        }

        return beanList;
    }

    @Override
    public int addGroup(GroupBean group) {
        GroupEntity entity = toEntity(group);
        if ((entity == null) || StrUtil.isBlank(entity.getName())) {
            return -1;
        }

        entity.setGroupId(IDGenerator.getId());
        entity.setStatus(R.groupStatus.CREATED);

        groupDao.insert(entity);
        logger.info("添加执行器组成功，name: {}, groupId: {}", entity.getName(), entity.getGroupId());
        return 1;
    }

    @Override
    public int updateGroup(GroupBean group) {
        GroupEntity entity = toEntity(group);
        if ((entity == null) || StrUtil.isBlank(entity.getGroupId())) {
            return -1;
        }

        groupDao.update(entity);
        logger.info("更新执行器组成功，name: {}, groupId: {}", entity.getName(), entity.getGroupId());
        return 1;
    }

    @Override
    public int deleteGroup(String groupId) {
        if (StrUtil.isBlank(groupId)) {
            return -1;
        }

        GroupEntity entity = new GroupEntity();
        entity.setGroupId(groupId);
        entity.setStatus(R.groupStatus.DELETED);

        groupDao.update(entity);
        logger.info("删除执行器组成功，groupId: {}", entity.getGroupId());
        return 1;
    }

    @Override
    public int addToGroup(String groupId, String executorId) {
        if (StrUtil.isBlank(groupId) || StrUtil.isBlank(executorId)) {
            return -1;
        }

        if (groupDao.selectExecutorId(groupId, executorId).isEmpty()) {
            groupDao.addExecutorToGroup(groupId, executorId);
        }

        logger.info("执行器添加执行器组成功，groupId: {}, executorId: {}", groupId, executorId);
        return 1;
    }

    @Override
    public int delFromGroup(String groupId, String executorId) {
        if (StrUtil.isBlank(groupId) || StrUtil.isBlank(executorId)) {
            return -1;
        }

        groupDao.deleteExecutorFromGroup(groupId, executorId);
        logger.info("执行器组删除执行器成功，groupId: {}, executorId: {}", groupId, executorId);
        return 1;
    }

    /**
     * Bean转换类
     */
    static class GroupConvert {

        static GroupBean toBean(GroupEntity entity) {
            GroupBean bean = null;

            if (entity != null) {
                bean = new GroupBean();
                BeanUtil.copyProperties(bean, entity);
            }
            return bean;
        }

        static GroupEntity toEntity(GroupBean bean) {
            GroupEntity entity = null;

            if (bean != null) {
                entity = new GroupEntity();
                BeanUtil.copyProperties(bean, entity);
            }
            return entity;
        }

    }

}

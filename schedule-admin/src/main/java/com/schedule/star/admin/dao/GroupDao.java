package com.schedule.star.admin.dao;

import com.schedule.star.admin.entity.ExecutorEntity;
import com.schedule.star.admin.entity.GroupEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xiangnan
 * @date 2018/3/16 13:40
 */
@Mapper
public interface GroupDao {

    List<GroupEntity> selectList();

    GroupEntity selectByGroupId(@Param("groupId") String groupId);

    int insert(GroupEntity entity);

    int update(GroupEntity entity);

    int addExecutorToGroup(@Param("groupId") String groupId, @Param("executorId") String executorId);

    List<String> selectExecutorIdByGroupId(@Param("groupId") String groupId);

    List<ExecutorEntity> selectExecutorListByGroupId(@Param("groupId") String groupId);

    List<String> selectExecutorId(@Param("groupId") String groupId, @Param("executorId") String executorId);

    int deleteExecutorFromGroup(@Param("groupId") String groupId, @Param("executorId") String executorId);

}

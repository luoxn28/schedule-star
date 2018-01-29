package com.schedule.star.admin.dao;

import com.schedule.star.admin.entity.ExecutorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author xiangnan
 * @date 2018/1/29 18:19
 */
@Mapper
public interface ExecutorDao {

    int insert(ExecutorEntity entity);

    ExecutorEntity selectByExecutorId(@Param("executorId") String executorId);

    ExecutorEntity selectByIpPort(@Param("ip") String ip, @Param("port") int port);

}

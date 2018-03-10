package com.schedule.star.admin.dao;

import com.schedule.star.admin.entity.JobEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xiangnan
 * @date 2018/3/10 19:02
 */
@Mapper
public interface JobDao {

    List<JobEntity> selectListByType(@Param("type") String type);

    JobEntity selectByJobId(@Param("jobId") String jobId);

    int insert(JobEntity entity);

    int update(JobEntity entity);

}

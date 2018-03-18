package com.schedule.star.admin.dao;

import com.schedule.star.admin.entity.LogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author xiangnan
 * @date 2018/3/18 14:18
 */
@Mapper
public interface LogDao {

    LogEntity selectByLogId(@Param("logId") String logId);

    int insert(LogEntity entity);

    int updateTriggerInfo(@Param("result") String result, @Param("content") String content,
                          @Param("ipPort") String ipPort, @Param("logId") String logId);

    int updateHandleInfo(@Param("handleResult") String handleResult, @Param("handleContent") String handleContent,
                         @Param("handleTime") Date handleTime, @Param("finishTime") Date finishTime,
                         @Param("logId") String logId);

    /**
     * 删除log记录，慎用。。。
     */
    int deleteByLogId(@Param("logId") String logId);

}

package com.schedule.star.admin.service;

import com.schedule.star.admin.bean.ExecutorBean;

import java.util.List;

/**
 * @author xiangnan
 * @date 2018/1/31 22:09
 */
public interface ExecutorService {

    List<ExecutorBean> getExecutorList();

    int updateExecutor(ExecutorBean bean);

    int deleteExecutor(String executorId);

}

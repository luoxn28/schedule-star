package com.schedule.star.admin.service;

import com.schedule.star.admin.bean.GroupBean;

import java.util.List;

/**
 * @author xiangnan
 * @date 2018/3/17 11:27
 */
public interface GroupService {

    List<GroupBean> getGroupList();

    int addGroup(GroupBean group);

    int updateGroup(GroupBean group);

    int deleteGroup(String groupId);

    int addToGroup(String groupId, String executorId);

    int delFromGroup(String groupId, String executorId);

}

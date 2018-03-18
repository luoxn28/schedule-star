package com.test.admin.service;

import com.schedule.star.admin.AdminApplication;
import com.schedule.star.admin.bean.GroupBean;
import com.schedule.star.admin.service.GroupService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author xiangnan
 * @date 2018/3/18 13:54
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AdminApplication.class)
public class GroupServiceTest {

    @Resource
    GroupService groupService;

    GroupBean groupBean = new GroupBean();

    @Before
    public void before() {
        groupBean.setName("group");
        groupBean.setGroupId("1803181358171959192.168.197.1");
    }

    @Test
    public void addGroup() {
//        groupService.addGroup(groupBean);

//        groupBean.setDesc("desc");
//        groupService.updateGroup(groupBean);

        groupService.deleteGroup("1803181358171959192.168.197.1");
    }

}

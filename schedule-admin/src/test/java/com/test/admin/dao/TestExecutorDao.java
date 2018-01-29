package com.test.admin.dao;

import cn.hutool.core.lang.Assert;
import com.schedule.star.admin.AdminApplication;
import com.schedule.star.admin.dao.ExecutorDao;
import com.schedule.star.admin.entity.ExecutorEntity;
import com.schedule.star.core.util.IDGenerator;
import com.schedule.star.core.util.R;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author xiangnan
 * @date 2018/1/29 19:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AdminApplication.class)
public class TestExecutorDao {

    @Resource
    private ExecutorDao executorDao;

    @Test
    public void testInsert() {
        ExecutorEntity entity = new ExecutorEntity();
        entity.setExecutorId(IDGenerator.getId());
        entity.setIp("127.0.0.1" + IDGenerator.getId());
        entity.setPort(9999);
        entity.setKeepAliveTime(10);
        entity.setToken(IDGenerator.getId());
        entity.setStatus(R.status.SUCCESS);

        Assert.isTrue(executorDao.insert(entity) > 0);
    }

    @Test
    public void testSelect() {
        executorDao.selectByExecutorId(IDGenerator.getId());

        System.out.println(executorDao.selectByExecutorId("1801291935125968192.168.6.89"));
        System.out.println(executorDao.selectByIpPort("127.0.0.1", 9999));
    }

}

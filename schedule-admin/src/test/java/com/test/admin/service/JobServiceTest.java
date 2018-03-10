package com.test.admin.service;

import com.schedule.star.admin.AdminApplication;
import com.schedule.star.admin.bean.JobBean;
import com.schedule.star.admin.service.JobService;
import com.schedule.star.core.util.IDGenerator;
import com.schedule.star.core.util.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author xiangnan
 * @date 2018/3/10 20:25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AdminApplication.class)
public class JobServiceTest {

    @Resource
    JobService jobService;

    JobBean jobBean = new JobBean();

    @Before
    public void before() {
        jobBean.setJobId("1803102029353581192.168.236.1");
        jobBean.setGroupId("1803102029358413192.168.236.1");
        jobBean.setName("测试任务222");
        jobBean.setType(R.jobType.Script);
        jobBean.setCron("0/3 * * * * ?");
    }

    @Test
    public void addJob() {
//        jobService.postJob(jobBean);
    }

    @Test
    public void putJob() {
        jobService.putJob(jobBean);
    }

}

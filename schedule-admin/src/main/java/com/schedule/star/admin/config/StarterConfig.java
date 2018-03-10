package com.schedule.star.admin.config;

import com.schedule.star.admin.component.JobTrigger;
import com.schedule.star.admin.component.bean.QzJobBean;
import com.schedule.star.core.util.SystemUtil;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;

/**
 * @author xiangnan
 * @date 2018/3/10 18:42
 */
@Configuration
public class StarterConfig {

    /**
     * quartz配置
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setStartupDelay(20);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContextKey");
        schedulerFactoryBean.setConfigLocation(new ClassPathResource("quartz.properties"));

        return schedulerFactoryBean;
    }

    @Bean(name = "internalScheduler")
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) {
        return schedulerFactoryBean.getScheduler();
    }

    /**
     * 需要先注入JobTrigger供JobBean使用
     */
    @Bean
    public QzJobBean qzJobBean(JobTrigger jobTrigger) {
        return new QzJobBean(jobTrigger);
    }

    /**
     * 配置spring mvc文件上传临时目录
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(SystemUtil.applicationPath);
        return factory.createMultipartConfig();
    }

}

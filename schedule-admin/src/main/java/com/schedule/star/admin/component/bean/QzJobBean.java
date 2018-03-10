package com.schedule.star.admin.component.bean;

import com.schedule.star.admin.component.JobTrigger;
import lombok.NoArgsConstructor;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author xiangnan
 * @date 2018/3/10 19:48
 */
@NoArgsConstructor
public class QzJobBean extends QuartzJobBean {

    private static JobTrigger jobTrigger;

    public QzJobBean(JobTrigger jobTrigger) {
        QzJobBean.jobTrigger = jobTrigger;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        jobTrigger.trigger(context);
    }

}

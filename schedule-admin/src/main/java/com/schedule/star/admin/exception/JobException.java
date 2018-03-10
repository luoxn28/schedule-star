package com.schedule.star.admin.exception;

import com.schedule.star.admin.exception.enums.JobEnum;
import com.schedule.star.core.exception.BaseException;

/**
 * @author xiangnan
 * @date 2018/2/11 21:59
 */
public class JobException extends BaseException {

    public JobException(JobEnum en) {
        super(en.getCode(), en.getMsg());
    }

    public JobException(JobEnum en, String msg) {
        super(en.getCode(), msg);
    }

}

package com.schedule.star.admin.exception;

import com.schedule.star.admin.exception.enums.JobInfoEnum;
import com.schedule.star.core.exception.BaseException;

/**
 * @author xiangnan
 * @date 2018/2/11 21:59
 */
public class JobInfoException extends BaseException {

    public JobInfoException(JobInfoEnum en) {
        super(en.getCode(), en.getMsg());
    }

    public JobInfoException(JobInfoEnum en, String msg) {
        super(en.getCode(), msg);
    }

}

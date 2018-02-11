package com.schedule.star.core.exception;

import lombok.Data;

/**
 * @author xiangnan
 * @date 2018/2/11 21:57
 */
@Data
public class BaseException extends RuntimeException {

    private int code;

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

}

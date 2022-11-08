package com.general.template.core.exception;


/**
 * 数据验证异常
 */
public class ValidationDataException extends RuntimeException {

    public ValidationDataException(String message) {
        super(message);
    }

    public ValidationDataException(String message, Throwable cause) {
        super(message, cause);
    }
}

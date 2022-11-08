package com.general.template.core.exception;

/**
 * 参数校验异常类
 */
public class ParamsValidationException extends RuntimeException {

    public ParamsValidationException(String message) {
        super(message);
    }

    public ParamsValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.general.template.core.exception;

/**
 * 文件操作相关异常
 */
public class FileExecuteException extends RuntimeException {

    public FileExecuteException(String message) {
        super(message);
    }

    public FileExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

}

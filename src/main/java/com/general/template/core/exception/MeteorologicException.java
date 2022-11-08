package com.general.template.core.exception;

import com.general.template.core.ResultResponseCode;
import lombok.Getter;

/**
 * 业务级异常
 */
@Getter
public class MeteorologicException extends RuntimeException {
    private com.general.template.core.ResultResponseCode ResultResponseCode;

    public MeteorologicException(ResultResponseCode ResultResponseCode) {
        super();
        this.ResultResponseCode = ResultResponseCode;
    }

    public MeteorologicException(String message) {
        super(message);
    }

    public MeteorologicException(String message, Throwable cause) {
        super(message, cause);
    }
}

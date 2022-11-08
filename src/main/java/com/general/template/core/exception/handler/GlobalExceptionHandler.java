package com.general.template.core.exception.handler;

import com.alibaba.fastjson.JSONObject;
import com.general.template.core.ResultResponse;
import com.general.template.core.ResultResponseCode;
import com.general.template.core.exception.FileExecuteException;
import com.general.template.core.exception.MeteorologicException;
import com.general.template.core.exception.ParamsValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一接口错误拦截处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 统一的错误处理方法
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResultResponse defaultErrorHandler(HttpServletRequest req, Exception e) {
        String errorPosition = "";
        // 如果错误堆栈信息存在
        if (e.getStackTrace().length > 0) {
            StackTraceElement element = e.getStackTrace()[0];
            String fileName = element.getFileName() == null ? "未找到错误文件" : element.getFileName();
            int lineNumber = element.getLineNumber();
            errorPosition = fileName + ":" + lineNumber;
        }

        JSONObject errorObject = new JSONObject();
        errorObject.put("errorLocation", e + " 错误位置:" + errorPosition);

        log.error("异常", e);
        return ResultResponse.of(ResultResponseCode.INTERNAL_SERVER_ERROR, errorObject);
    }

    /**
     * 请求方法不支持
     *
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultResponse httpRequestMethodHandler() {
        return ResultResponse.of(ResultResponseCode.METHOD_NOT_ALLOWED);
    }

    /**
     * 参数校验失败
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResultResponse.of(ResultResponseCode.BAD_REQUEST);
    }

    /**
     * 参数缺失
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResultResponse missingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ResultResponse.of(ResultResponseCode.BAD_REQUEST);
    }

    /**
     * 参数校验失败
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ParamsValidationException.class)
    public ResultResponse validationException(ParamsValidationException e) {
        return ResultResponse.of(ResultResponseCode.BAD_REQUEST);
    }

    /**
     * 参数校验失败
     *
     * @param e
     * @return
     */
    @ExceptionHandler(FileExecuteException.class)
    public ResultResponse fileExecuteException(FileExecuteException e) {
        return ResultResponse.of(ResultResponseCode.PRECONDITION_FAILED);
    }

    /**
     * 系统业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MeteorologicException.class)
    public ResultResponse meteorologicException(MeteorologicException e) {
        return ResultResponse.of(ResultResponseCode.BIZ_BASE_ERROR);
    }

}

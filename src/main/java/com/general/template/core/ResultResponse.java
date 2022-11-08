package com.general.template.core;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 响应信息主体
 */
@ApiModel(value = "ResultResponse", description = "响应对象")
@Builder
@Data
@AllArgsConstructor
public class ResultResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "响应标记")
    @Builder.Default
    private int code = ResultResponseCode.OK.value();

    @ApiModelProperty(value = "响应信息")
    @Builder.Default
    private String message = "success";

    @ApiModelProperty(value = "响应数据")
    private T data;

    public ResultResponse() {
        super();
    }

    public ResultResponse(T data) {
        super();
        this.data = data;
    }

    public ResultResponse(String message, int code) {
        super();
        this.code = code;
        this.message = message;
    }

    public ResultResponse(T data, String message) {
        super();
        this.data = data;
        this.message = message;
    }

    public ResultResponse(T data, int code) {
        super();
        this.data = data;
        this.code = code;
    }

    public ResultResponse(Throwable e) {
        super();
        this.message = e.getMessage();
        this.code = ResultResponseCode.INTERNAL_SERVER_ERROR.value();
    }

    public static <T> ResultResponse<T> of() {
        return ResultResponse.<T>builder().build();
    }

    public static <T> ResultResponse<T> of(T data) {
        return ResultResponse.<T>builder().data(data).build();
    }

    public static <T> ResultResponse<T> of(ResultResponseCode responseCode) {
        return ResultResponse.<T>builder().message(responseCode.getDec()).code(responseCode.value()).build();
    }

    public static <T> ResultResponse<T> of(ResultResponseCode responseCode, T data) {
        return ResultResponse.<T>builder().message(responseCode.getDec()).code(responseCode.value()).data(data).build();
    }

    public static <T> ResultResponse<T> of(String message, int code) {
        return ResultResponse.<T>builder().message(message).code(code).build();
    }

    public static <T> ResultResponse<T> of(T data, String message) {
        return ResultResponse.<T>builder().data(data).message(message).build();
    }

    public static <T> ResultResponse<T> error() {
        return ResultResponse.<T>builder().code(ResultResponseCode.INTERNAL_SERVER_ERROR.value()).build();
    }

    public static <T> ResultResponse<T> error(String message) {
        return ResultResponse.<T>builder().code(ResultResponseCode.INTERNAL_SERVER_ERROR.value()).message(message).build();
    }

}

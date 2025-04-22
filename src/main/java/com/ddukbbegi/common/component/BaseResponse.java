package com.ddukbbegi.common.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> implements Serializable {

    @JsonProperty("result")
    private JSONResult jsonResult; // 응답 상태

    private T data; // 응답 데이터

    private BaseResponse(T data, ResultCode resultCode) {
        this.jsonResult = JSONResult.success(resultCode);
        this.data = data;
    }

    private BaseResponse(JSONResult jsonResult) {
        this.jsonResult = jsonResult;
        this.data = null;
    }

    public static <T> BaseResponse<T> success(T data, ResultCode resultCode) {
        return new BaseResponse<>(data, resultCode);
    }

    public static <T> BaseResponse<T> error(ResultCode resultCode, Exception e) {
        return new BaseResponse<>(JSONResult.failure(resultCode, e));
    }

    public static <T> BaseResponse<T> error(ResultCode resultCode, String customMessage) {
        return new BaseResponse<>(JSONResult.failure(resultCode, customMessage));
    }
}

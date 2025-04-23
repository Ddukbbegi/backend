package com.ddukbbegi.common.exception;

import com.ddukbbegi.common.component.ResultCode;
import lombok.Getter;

public class BusinessException extends RuntimeException {

    @Getter
    private final ResultCode resultCode;

    public BusinessException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }

}

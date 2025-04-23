package com.ddukbbegi.common.component;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JSONResult {

    private int status; // HTTP 상태 코드

    private String code; // 응답 코드

    private String message; // 응답 메시지

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp; // 시간

    /**
     * 응답 코드에 따른 Success
     */
    public static JSONResult success(ResultCode resultCode) {
        return JSONResult.builder()
                .status(resultCode.getStatus().value())
                .code(resultCode.getCode())
                .message(resultCode.getDefaultMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 예외 객체의 메시지를 활용하여 실패 응답 생성.
     * 예외 메시지가 있으면 해당 메시지를, 없으면 기본 메시지를 사용.
     */
    public static JSONResult failure(ResultCode resultCode, Exception ex) {
        String message = (ex != null && StringUtils.isNotBlank(ex.getMessage()))
                ? ex.getMessage() : resultCode.getDefaultMessage();
        return JSONResult.builder()
                .status(resultCode.getStatus().value())
                .code(resultCode.getCode())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 사용자 정의 메시지를 전달하여 실패 응답 생성.
     */
    public static JSONResult failure(ResultCode resultCode, String customMessage) {
        String message = StringUtils.isNotBlank(customMessage) ? customMessage : resultCode.getDefaultMessage();
        return JSONResult.builder()
                .status(resultCode.getStatus().value())
                .code(resultCode.getCode())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

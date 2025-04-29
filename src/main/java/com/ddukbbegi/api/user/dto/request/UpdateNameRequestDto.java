package com.ddukbbegi.api.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateNameRequestDto(
        @NotBlank(message = "이름은 필수 입력값입니다.")
        @Size(max = 10, message = "이름은 10자 이내로 입력해주세요.")
        String name) {
}

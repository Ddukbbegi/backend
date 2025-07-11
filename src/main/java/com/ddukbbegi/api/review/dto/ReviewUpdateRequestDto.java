package com.ddukbbegi.api.review.dto;

import jakarta.validation.constraints.*;

public record ReviewUpdateRequestDto(
        @NotBlank(message = "리뷰 내용은 필수입니다.")
        @Size(min = 1, max = 300, message = "내용은 1자 이상 300자 이하로 입력해주세요.")
        String contents,

        @DecimalMin(value = "0.0", message = "최소 평점은 0.0입니다.")
        @DecimalMax(value = "5.0", message = "최대 평점은 5.0입니다.")
        Float rate,

        @NotNull(message = "익명 여부를 선택해주세요.")
        AnonymousStatus anonymousStatus
) { }

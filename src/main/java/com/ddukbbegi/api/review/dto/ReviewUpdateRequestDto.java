package com.ddukbbegi.api.review.dto;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateRequestDto{
    @NotBlank(message = "리뷰 내용은 필수입니다.")
    private String contents;
    @DecimalMin(value = "0.0", message = "최소 평점은 0.0입니다.")
    @DecimalMax(value = "5.0", message = "최대 평점은 5.0입니다.")
    private Float rate;
    @NotNull(message = "익명 여부를 선택해주세요.")
    private AnonymousStatus anonymousStatus;
}

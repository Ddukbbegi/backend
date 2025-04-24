package com.ddukbbegi.api.review.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

    @Positive
    private Long orderId;
    @NotBlank(message = "리뷰 내용은 필수입니다.")
    @Size(min = 1, max = 300, message = "내용은 1자 이상 300자 이하로 입력해주세요.")
    private String contents;
    @NotNull(message = "별점을 입력해주세요.")
    @DecimalMin(value = "0.0", message = "최소 평점은 0.0입니다.")
    @DecimalMax(value = "5.0", message = "최대 평점은 5.0입니다.")
    private Float rate;
    @NotNull(message = "익명 여부를 선택해주세요.")
    private AnonymousStatus anonymousStatus;

}

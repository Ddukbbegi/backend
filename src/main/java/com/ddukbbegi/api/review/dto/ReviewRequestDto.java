package com.ddukbbegi.api.review.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

    private Long orderId;
    private String contents;
    private Float rate;
    private AnonymousStatus anonymousStatus;

}

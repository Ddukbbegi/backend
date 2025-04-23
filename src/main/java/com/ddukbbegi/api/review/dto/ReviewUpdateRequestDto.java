package com.ddukbbegi.api.review.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateRequestDto{

    private String contents;
    private Float rate;
    private AnonymousStatus anonymousStatus;
}

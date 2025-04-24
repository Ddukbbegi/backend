package com.ddukbbegi.api.review.dto;

import com.ddukbbegi.api.review.entity.Review;

public class ReviewWithLikeCountDto {
    private final Review review;
    private final Long likeCount;

    public ReviewWithLikeCountDto(Review review, Long likeCount) {
        this.review = review;
        this.likeCount = likeCount;
    }



    public ReviewResponseDto from(){
        return ReviewResponseDto.from(this.review, this.likeCount);
    }
}
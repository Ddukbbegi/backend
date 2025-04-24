package com.ddukbbegi.api.review.dto;

public class ReviewLikeCountDto {
    private Long reviewId;
    private Long likeCount;

    public ReviewLikeCountDto(Long reviewId, Long likeCount) {
        this.reviewId = reviewId;
        this.likeCount = likeCount;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public Long getLikeCount() {
        return likeCount;
    }
}

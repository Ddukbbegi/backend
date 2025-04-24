package com.ddukbbegi.api.review.dto;

import com.ddukbbegi.api.review.entity.Review;

public record ReviewResponseDto(
        Long reviewId,
        Long orderId,
        String contents,
        Float rate,
        String writer,
        String reply,
        Long likeCount
) {

    public static ReviewResponseDto from(Review review, Long likeCount) {
        String writer = review.getAnonymousStatus() == AnonymousStatus.ANONYMOUS
                ? "익명"
                : review.getUser().getEmail(); // 추후 review.getUser().getNickname() 으로 교체
        likeCount = likeCount == null
                ? 0
                : likeCount;
        return new ReviewResponseDto(
                review.getId(),
                review.getOrder().getId(), // 추후 추가
                review.getContents(),
                review.getRate(),
                writer,
                review.getReply(),
                likeCount
        );
    }
}

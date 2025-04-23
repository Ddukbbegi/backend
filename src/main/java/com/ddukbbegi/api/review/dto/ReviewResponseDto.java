package com.ddukbbegi.api.review.dto;


import com.ddukbbegi.api.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReviewResponseDto {

    private final Long reviewId;
    private final Long orderId;
    private final String contents;
    private final Float rate;
    private final String writer;
    private final String reply;

    public static ReviewResponseDto from(Review review) {
        return ReviewResponseDto.builder()
                .reviewId(review.getId())
                .orderId( /* review.getOrder().getId() */ null ) // 나중에 Order 연관관계 생기면 수정
                .contents(review.getContents())
                .rate(review.getRate())
                .writer( /* review.getUser().getNickname() */ null ) // 나중에 User 연관관계 생기면 수정
                .reply(review.getReply())
                .build();
    }
}

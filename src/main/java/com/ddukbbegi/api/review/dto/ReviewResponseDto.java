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
        String writer = review.getAnonymousStatus() == AnonymousStatus.ANONYMOUS
                ? "익명"
                : review.getUser().getEmail(); // 추후 review.getUser().getNickname() 으로 교체

        return ReviewResponseDto.builder()
                .reviewId(review.getId())
                .orderId(/* review.getOrder().getId() */ null) // 추후 추가
                .contents(review.getContents())
                .rate(review.getRate())
                .writer(writer)
                .reply(review.getReply())
                .build();
    }

}

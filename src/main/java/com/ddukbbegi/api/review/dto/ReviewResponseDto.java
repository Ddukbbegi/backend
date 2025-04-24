package com.ddukbbegi.api.review.dto;


import com.ddukbbegi.api.review.entity.Reviews;
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

    public static ReviewResponseDto from(Reviews reviews) {
        String writer = reviews.getAnonymousStatus() == AnonymousStatus.ANONYMOUS
                ? "익명"
                : reviews.getUser().getEmail(); // 추후 review.getUser().getNickname() 으로 교체

        return ReviewResponseDto.builder()
                .reviewId(reviews.getId())
                .orderId(/* review.getOrder().getId() */ null) // 추후 추가
                .contents(reviews.getContents())
                .rate(reviews.getRate())
                .writer(writer)
                .reply(reviews.getReply())
                .build();
    }

}

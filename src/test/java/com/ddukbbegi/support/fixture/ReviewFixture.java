package com.ddukbbegi.support.fixture;

import com.ddukbbegi.api.review.dto.AnonymousStatus;
import com.ddukbbegi.api.review.dto.ReviewRequestDto;

public class ReviewFixture {

    public static ReviewRequestDto createReviewRequestDto(Long orderId, String contents) {
        return new ReviewRequestDto(
                orderId,
                contents,
                4.5f,
                AnonymousStatus.ANONYMOUS);
    }
}

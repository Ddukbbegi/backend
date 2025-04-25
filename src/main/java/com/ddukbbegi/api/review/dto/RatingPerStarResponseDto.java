package com.ddukbbegi.api.review.dto;


import java.util.List;

public record RatingPerStarResponseDto(
        Long storeId,
        Float rate,
        List<Long> ratingCountPerStar
) {

}

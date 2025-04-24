package com.ddukbbegi.api.review.controller;


import com.ddukbbegi.api.common.dto.PageResponseDto;
import com.ddukbbegi.api.review.dto.ReviewOwnerRequestDto;
import com.ddukbbegi.api.review.dto.ReviewRequestDto;
import com.ddukbbegi.api.review.dto.ReviewResponseDto;
import com.ddukbbegi.api.review.dto.ReviewUpdateRequestDto;
import com.ddukbbegi.api.review.service.ReviewService;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public BaseResponse<ReviewResponseDto> saveReview(
            @Valid @RequestBody ReviewRequestDto requestDto
    ){

        ReviewResponseDto responseDto = reviewService.saveReview(1L, requestDto);
        return BaseResponse.success(responseDto,ResultCode.CREATED);
    }


    @GetMapping("/users/reviews")
    public BaseResponse<PageResponseDto<ReviewResponseDto>> findAllMyReviews(
            //유저 정보 추후 인증인가 적용후 구현
            Pageable pageable
    ){
        Page<ReviewResponseDto> responseDtoPage = reviewService.findAllMyReviews(1L, pageable);
        PageResponseDto<ReviewResponseDto> dto = PageResponseDto.toDto(responseDtoPage);
        return BaseResponse.success(dto,ResultCode.OK);
    }

    @PatchMapping("/reviews/{reviewId}")
    public BaseResponse<ReviewResponseDto> updateReview(
            @Positive @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateRequestDto requestDto
    ){

        ReviewResponseDto responseDto = reviewService.updateReview(1L, reviewId, requestDto);
        return BaseResponse.success(responseDto, ResultCode.OK);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public BaseResponse<Void> deleteReview(@Positive @PathVariable Long reviewId){
        {   reviewService.deleteReview(reviewId);
            return BaseResponse.success(ResultCode.NO_CONTENT);
        }
    }

    @PostMapping("/owners/reivews/{reviewId}/reply")
    public BaseResponse<ReviewResponseDto> saveReviewReply(
            @Positive @PathVariable Long reviewId,
            @Valid @RequestBody ReviewOwnerRequestDto requestDto
    ){
        Long ownerId = 2L;
        ReviewResponseDto responseDto = reviewService.saveReviewReply(ownerId, reviewId, requestDto);
        return BaseResponse.success(responseDto, ResultCode.OK);
    }
    @PatchMapping("/owners/reivews/{reviewId}/reply-update")
    public BaseResponse<ReviewResponseDto> updateReviewReply(
            @Positive @PathVariable Long reviewId,
            @Valid @RequestBody ReviewOwnerRequestDto requestDto
    ){
        Long ownerId = 2L;
        ReviewResponseDto responseDto = reviewService.updateReviewReply(ownerId, reviewId, requestDto);
        return BaseResponse.success(responseDto, ResultCode.OK);
    }
    @DeleteMapping("/owners/reivews/{reviewId}/reply-delete")
    public BaseResponse<Void> deleteReviewReply(
            @Positive @PathVariable Long reviewId
    ){
        Long ownerId = 2L;
        reviewService.deleteReviewReply(ownerId, reviewId);
        return BaseResponse.success(ResultCode.NO_CONTENT);
    }

    @PostMapping("/reviews/{reviewId}/likes")
    public BaseResponse<Void> saveLike(
            @Positive @PathVariable Long reviewId
    ){
        Long userId = 1L;
        reviewService.saveLike(userId, reviewId);
        return BaseResponse.success(ResultCode.OK);
    }
    @DeleteMapping("/reviews/{reviewId}/likes")
    public BaseResponse<Void> deleteLike(
            @Positive @PathVariable Long reviewId
    ){
        Long userId = 1L;
        reviewService.deleteLike(userId, reviewId);
        return BaseResponse.success(ResultCode.OK);
    }


}

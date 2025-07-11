package com.ddukbbegi.api.review.controller;


import com.ddukbbegi.api.common.dto.PageResponseDto;
import com.ddukbbegi.api.review.dto.*;
import com.ddukbbegi.api.review.service.ReviewService;
import com.ddukbbegi.api.user.entity.CustomUserDetails;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ReviewRequestDto requestDto
    ){

        ReviewResponseDto responseDto = reviewService.saveReview(userDetails.getUserId(), requestDto);
        return BaseResponse.success(responseDto,ResultCode.CREATED);
    }


    @GetMapping("/users/reviews")
    public BaseResponse<PageResponseDto<ReviewResponseDto>> findAllMyReviews(
            //유저 정보 추후 인증인가 적용후 구현
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(page = 0, size = 10, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable
    ){
            Page<ReviewResponseDto> responseDtoPage = reviewService.findAllMyReviews(userDetails.getUserId(), pageable);
        PageResponseDto<ReviewResponseDto> dto = PageResponseDto.toDto(responseDtoPage);
        return BaseResponse.success(dto,ResultCode.OK);
    }
    //가게리뷰 전체조회
    @GetMapping("/stores/{storeId}/reviews")
    public BaseResponse<PageResponseDto<ReviewResponseDto>> findAllStoreReviews(
            @Positive @PathVariable Long storeId,
            @PageableDefault(page = 0, size = 10, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy  // 정렬 기준을 동적으로 받기
    ){
        if ("likeCount".equalsIgnoreCase(sortBy)) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by(Sort.Order.desc("likeCount"), Sort.Order.desc("createdAt")));
        }

        Page<ReviewResponseDto> responseDtoPage = reviewService.findAllStoreReviews(storeId, pageable);
        PageResponseDto<ReviewResponseDto> dto = PageResponseDto.toDto(responseDtoPage);
        return BaseResponse.success(dto, ResultCode.OK);
    }

    @PatchMapping("/reviews/{reviewId}")
    public BaseResponse<ReviewResponseDto> updateReview(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Positive @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateRequestDto requestDto
    ){

        ReviewResponseDto responseDto = reviewService.updateReview(userDetails.getUserId(), reviewId, requestDto);
        return BaseResponse.success(responseDto, ResultCode.OK);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public BaseResponse<Void> deleteReview(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Positive @PathVariable Long reviewId
    ){
        {
            reviewService.deleteReview(userDetails.getUserId(), reviewId);
            return BaseResponse.success(ResultCode.NO_CONTENT);
        }
    }

    @PostMapping("/owners/reviews/{reviewId}/reply")
    public BaseResponse<ReviewResponseDto> saveReviewReply(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Positive @PathVariable Long reviewId,
            @Valid @RequestBody ReviewOwnerRequestDto requestDto
    ){
        ReviewResponseDto responseDto = reviewService.saveReviewReply(userDetails.getUserId(), reviewId, requestDto);
        return BaseResponse.success(responseDto, ResultCode.OK);
    }
    @PatchMapping("/owners/reviews/{reviewId}/reply-update")
    public BaseResponse<ReviewResponseDto> updateReviewReply(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Positive @PathVariable Long reviewId,
            @Valid @RequestBody ReviewOwnerRequestDto requestDto
    ){
        ReviewResponseDto responseDto = reviewService.updateReviewReply(userDetails.getUserId(),reviewId, requestDto);
        return BaseResponse.success(responseDto, ResultCode.OK);
    }
    @DeleteMapping("/owners/reviews/{reviewId}/reply-delete")
    public BaseResponse<Void> deleteReviewReply(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Positive @PathVariable Long reviewId
    ){
        reviewService.deleteReviewReply(userDetails.getUserId(), reviewId);
        return BaseResponse.success(ResultCode.NO_CONTENT);
    }

    @PostMapping("/reviews/{reviewId}/likes")
    public BaseResponse<Void> saveLike(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Positive @PathVariable Long reviewId
    ){
        reviewService.saveLike(userDetails.getUserId(), reviewId);
        return BaseResponse.success(ResultCode.OK);
    }
    @DeleteMapping("/reviews/{reviewId}/likes")
    public BaseResponse<Void> deleteLike(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Positive @PathVariable Long reviewId
    ){
        reviewService.deleteLike(userDetails.getUserId(), reviewId);
        return BaseResponse.success(ResultCode.OK);
    }

    @GetMapping("/stores/{storeId}/rates")
    public BaseResponse<RatingPerStarResponseDto> getStoreRate(
            @Positive @PathVariable Long storeId
    ){
        RatingPerStarResponseDto responseDto = reviewService.getStoreRating(storeId);
        return BaseResponse.success(responseDto, ResultCode.OK);
    }


}

package com.ddukbbegi.api.review.controller;


import com.ddukbbegi.api.common.dto.PageResponseDto;
import com.ddukbbegi.api.review.dto.ReviewRequestDto;
import com.ddukbbegi.api.review.dto.ReviewResponseDto;
import com.ddukbbegi.api.review.dto.ReviewUpdateRequestDto;
import com.ddukbbegi.api.review.service.ReviewService;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public BaseResponse<ReviewResponseDto> saveReview(
            @RequestBody ReviewRequestDto requestDto
    ){

        ReviewResponseDto responseDto = reviewService.saveReview(1L, requestDto);
        return BaseResponse.success(responseDto,ResultCode.CREATED);
    }


    //나의 리뷰 조회
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
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequestDto requestDto
            ){

        ReviewResponseDto responseDto = reviewService.updateReview(1L, reviewId, requestDto);
        return BaseResponse.success(responseDto, ResultCode.OK);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public BaseResponse<Void> deleteReview(@PathVariable Long reviewId){
        {   reviewService.deleteReview(reviewId);
            return BaseResponse.success(ResultCode.NO_CONTENT);
        }
    }


}

package com.ddukbbegi.api.review.controller;


import com.ddukbbegi.api.common.dto.PageResponseDto;
import com.ddukbbegi.api.review.dto.ReviewRequestDto;
import com.ddukbbegi.api.review.dto.ReviewResponseDto;
import com.ddukbbegi.api.review.service.ReviewService;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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


}

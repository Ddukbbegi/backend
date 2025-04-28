package com.ddukbbegi.api.point.controller;

import com.ddukbbegi.api.point.component.PointEventListener;
import com.ddukbbegi.api.point.service.PointService;
import com.ddukbbegi.common.auth.CustomUserDetails;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/points")
public class PointController {

    private final PointService pointService;

    @GetMapping
    public BaseResponse<Long> getMyPoint(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Long myPoint = pointService.getMyPoint(userDetails.getUserId());
        return BaseResponse.success(myPoint, ResultCode.OK);
    }

}

package com.ddukbbegi.api.store.controller;

import com.ddukbbegi.api.store.service.StoreService;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/")
    public BaseResponse<?> success() {
        return BaseResponse.success(null, ResultCode.OK);
    }

    @GetMapping("/fail")
    public BaseResponse<?> fail() {
        storeService.get();
        return BaseResponse.success(null, ResultCode.OK);
    }

}

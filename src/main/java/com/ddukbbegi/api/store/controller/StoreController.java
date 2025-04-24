package com.ddukbbegi.api.store.controller;

import com.ddukbbegi.api.common.dto.PageResponseDto;
import com.ddukbbegi.api.store.dto.request.*;
import com.ddukbbegi.api.store.dto.response.OwnerStoreResponseDto;
import com.ddukbbegi.api.store.dto.response.StorePageItemResponseDto;
import com.ddukbbegi.api.store.dto.response.StoreRegisterAvailableResponseDto;
import com.ddukbbegi.api.store.dto.response.StoreIdResponseDto;
import com.ddukbbegi.api.store.service.StoreService;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/")
    public BaseResponse<?> success() {
    @PostMapping
    public BaseResponse<StoreIdResponseDto> registerStore(@RequestBody StoreRegisterRequestDto dto) {

        StoreIdResponseDto response = storeService.registerStore(dto);
        return BaseResponse.success(response, ResultCode.CREATED);
    }
        return BaseResponse.success(ResultCode.OK);
    }

    @GetMapping("/fail")
    public BaseResponse<?> fail() {
        storeService.get();
        return BaseResponse.success(ResultCode.OK);
    }

}

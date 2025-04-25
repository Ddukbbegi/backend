package com.ddukbbegi.api.store.controller;

import com.ddukbbegi.api.common.dto.PageResponseDto;
import com.ddukbbegi.api.store.dto.response.StoreDetailResponseDto;
import com.ddukbbegi.api.store.dto.response.StorePageItemResponseDto;
import com.ddukbbegi.api.store.service.StoreService;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public BaseResponse<PageResponseDto<StorePageItemResponseDto>> getStores(@RequestParam(defaultValue = "") String name,
                                                                             @PageableDefault Pageable pageable) {

        PageResponseDto<StorePageItemResponseDto> response = storeService.getStores(name, pageable);
        return BaseResponse.success(response, ResultCode.OK);
    }

    @GetMapping("/{storeId}")
    public BaseResponse<StoreDetailResponseDto> getStore(@PathVariable Long storeId) {

        StoreDetailResponseDto response = storeService.getStore(storeId);
        return BaseResponse.success(response, ResultCode.OK);
    }

}

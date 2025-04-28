package com.ddukbbegi.api.store.controller;

import com.ddukbbegi.api.store.dto.request.*;
import com.ddukbbegi.api.store.dto.response.OwnerStoreDetailResponseDto;
import com.ddukbbegi.api.store.dto.response.OwnerStoreResponseDto;
import com.ddukbbegi.api.store.dto.response.StoreIdResponseDto;
import com.ddukbbegi.api.store.dto.response.StoreRegisterAvailableResponseDto;
import com.ddukbbegi.api.store.service.StoreService;
import com.ddukbbegi.api.user.entity.CustomUserDetails;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner/stores")
public class StoreOwnerController {

    private final StoreService storeService;

    @PostMapping
    public BaseResponse<StoreIdResponseDto> registerStore(@RequestBody @Valid StoreRegisterRequestDto dto,
                                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        StoreIdResponseDto response = storeService.registerStore(dto, customUserDetails.getUserId());
        return BaseResponse.success(response, ResultCode.CREATED);
    }

    @GetMapping("/me/available")
    public BaseResponse<StoreRegisterAvailableResponseDto> checkStoreRegistrationAvailability(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        StoreRegisterAvailableResponseDto response = storeService.checkStoreRegistrationAvailability(customUserDetails.getUserId());
        return BaseResponse.success(response, ResultCode.OK);
    }

    @GetMapping
    public BaseResponse<List<OwnerStoreResponseDto>> getOwnerStoreList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        List<OwnerStoreResponseDto> response = storeService.getOwnerStoreList(customUserDetails.getUserId());
        return BaseResponse.success(response, ResultCode.OK);
    }

    @GetMapping("/{storeId}")
    public BaseResponse<OwnerStoreDetailResponseDto> getOwnerStoreDetail(@PathVariable(value = "storeId") Long storeId,
                                                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        OwnerStoreDetailResponseDto response = storeService.getOwnerStoreDetail(storeId, customUserDetails.getUserId());
        return BaseResponse.success(response, ResultCode.OK);
    }

    @PatchMapping("/{storeId}/basic-info")
    public BaseResponse<Void> updateStoreBasicInfo(@PathVariable Long storeId,
                                                   @RequestBody @Valid StoreUpdateBasicInfoRequestDto dto,
                                                   @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        storeService.updateStoreBasicInfo(storeId, dto, customUserDetails.getUserId());
        return BaseResponse.success(ResultCode.OK);
    }

    @PatchMapping("/{storeId}/operation-info")
    public BaseResponse<Void> updateStoreOperationInfo(@PathVariable Long storeId,
                                                       @RequestBody @Valid StoreUpdateOperationInfoRequestDto dto,
                                                       @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        storeService.updateStoreOperationInfo(storeId, dto, customUserDetails.getUserId());
        return BaseResponse.success(ResultCode.OK);
    }

    @PatchMapping("/{storeId}/order-settings")
    public BaseResponse<Void> updateStoreOrderSettings(@PathVariable Long storeId,
                                                       @RequestBody @Valid StoreUpdateOrderSettingsRequestDto dto,
                                                       @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        storeService.updateStoreOrderSettings(storeId, dto, customUserDetails.getUserId());
        return BaseResponse.success(ResultCode.OK);
    }

    @PatchMapping("/{storeId}/temporarily-close")
    public BaseResponse<Void> updateTemporarilyClosed(@PathVariable Long storeId,
                                                      @RequestBody @Valid StoreUpdateStatusRequest dto,
                                                      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        storeService.updateTemporarilyClosed(storeId, dto, customUserDetails.getUserId());
        return BaseResponse.success(ResultCode.OK);
    }

    @PatchMapping("/{storeId}/permanently-close")
    public BaseResponse<Void> updatePermanentlyClosed(@PathVariable Long storeId,
                                                      @RequestBody @Valid StoreUpdateStatusRequest dto,
                                                      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        storeService.updatePermanentlyClosed(storeId, dto, customUserDetails.getUserId());
        return BaseResponse.success(ResultCode.OK);
    }

}

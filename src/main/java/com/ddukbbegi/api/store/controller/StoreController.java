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
import jakarta.validation.Valid;
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

    @PostMapping
    public BaseResponse<StoreIdResponseDto> registerStore(@RequestBody @Valid StoreRegisterRequestDto dto) {

        StoreIdResponseDto response = storeService.registerStore(dto);
        return BaseResponse.success(response, ResultCode.CREATED);
    }

    @GetMapping("/me/available")
    public BaseResponse<StoreRegisterAvailableResponseDto> checkStoreRegistrationAvailability() {

        StoreRegisterAvailableResponseDto response = storeService.checkStoreRegistrationAvailability();
        return BaseResponse.success(response, ResultCode.OK);
    }

    @GetMapping("/me")
    public BaseResponse<List<OwnerStoreResponseDto>> getOwnerStoreList() {

        List<OwnerStoreResponseDto> response = storeService.getOwnerStoreList();
        return BaseResponse.success(response, ResultCode.OK);
    }

    @GetMapping
    public BaseResponse<PageResponseDto<StorePageItemResponseDto>> getStores(@RequestParam(defaultValue = "") String name,
                                                                             @PageableDefault Pageable pageable) {

        PageResponseDto<StorePageItemResponseDto> response = storeService.getStores(name, pageable);
        return BaseResponse.success(response, ResultCode.OK);
    }

    @PatchMapping("/{storeId}/basic-info")
    public BaseResponse<Void> updateStoreBasicInfo(@PathVariable Long storeId,
                                                   @RequestBody @Valid StoreUpdateBasicInfoRequestDto dto) {

        storeService.updateStoreBasicInfo(storeId, dto);
        return BaseResponse.success(ResultCode.OK);
    }

    @PatchMapping("/{storeId}/operation-info")
    public BaseResponse<Void> updateStoreOperationInfo(@PathVariable Long storeId,
                                                       @RequestBody @Valid StoreUpdateOperationInfoRequestDto dto) {

        storeService.updateStoreOperationInfo(storeId, dto);
        return BaseResponse.success(ResultCode.OK);
    }

    @PatchMapping("/{storeId}/order-settings")
    public BaseResponse<Void> updateStoreOrderSettings(@PathVariable Long storeId,
                                                       @RequestBody @Valid StoreUpdateOrderSettingsRequestDto dto) {

        storeService.updateStoreOrderSettings(storeId, dto);
        return BaseResponse.success(ResultCode.OK);
    }

    @PatchMapping("/{storeId}/temporarily-close")
    public BaseResponse<Void> updateTemporarilyClosed(@PathVariable Long storeId,
                                                      @RequestBody @Valid StoreUpdateStatusRequest dto) {

        storeService.updateTemporarilyClosed(storeId, dto);
        return BaseResponse.success(ResultCode.OK);
    }

    @PatchMapping("/{storeId}/permanently-close")
    public BaseResponse<Void> updatePermanentlyClosed(@PathVariable Long storeId,
                                                      @RequestBody @Valid StoreUpdateStatusRequest dto) {

        storeService.updatePermanentlyClosed(storeId, dto);
        return BaseResponse.success(ResultCode.OK);
    }

}

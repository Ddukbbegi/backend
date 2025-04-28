package com.ddukbbegi.api.store.service;

import com.ddukbbegi.api.common.dto.PageResponseDto;
import com.ddukbbegi.api.store.dto.request.*;
import com.ddukbbegi.api.store.dto.response.*;
import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.StoreStatus;
import com.ddukbbegi.api.store.repository.StoreRepository;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.common.component.ResultCode;
import com.ddukbbegi.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreStatusResolveService storeStatusResolveService;

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public StoreIdResponseDto registerStore(StoreRegisterRequestDto dto, Long userId) {

        if (!storeRepository.isStoreRegistrationAvailable(userId)) {
            throw new BusinessException(ResultCode.STORE_LIMIT_EXCEEDED);
        }

        User user = userRepository.findByIdOrElseThrow(userId);

        Store store = dto.toEntity(user);
        Store savedStore = storeRepository.save(store);

        return StoreIdResponseDto.of(savedStore.getId());
    }

    @Transactional(readOnly = true)
    public StoreRegisterAvailableResponseDto checkStoreRegistrationAvailability(Long userId) {

        boolean available = storeRepository.isStoreRegistrationAvailable(userId);
        return StoreRegisterAvailableResponseDto.of(available);
    }

    @Transactional(readOnly = true)
    public List<OwnerStoreResponseDto> getOwnerStoreList(Long userId) {

        List<Store> storeList = storeRepository.findAllByUser_Id(userId);
        return storeList.stream()
                .map(OwnerStoreResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public OwnerStoreDetailResponseDto getOwnerStoreDetail(Long storeId, Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        if (!Objects.equals(store.getUser().getId(), user.getId())) {
            throw new BusinessException(ResultCode.STORE_FORBIDDEN_ACCESS);
        }

        return OwnerStoreDetailResponseDto.fromEntity(store);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<StorePageItemResponseDto> getStores(String name, Pageable pageable) {

        Page<StorePageItemResponseDto> result = storeRepository.findAllStoreByName("%" + name + "%", pageable)
                .map(StorePageItemResponseDto::fromEntity);
        return PageResponseDto.toDto(result);
    }

    @Transactional(readOnly = true)
    public StoreDetailResponseDto getStore(Long storeId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        return StoreDetailResponseDto.fromEntity(store);
    }

    @Transactional
    public void updateStoreBasicInfo(Long storeId, StoreUpdateBasicInfoRequestDto dto, Long userId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        checkStoreOwnerPermission(store, userId);

        RequestStoreBasicInfo basicInfoDto = dto.basicInfoDto();
        store.updateBasicInfo(
                basicInfoDto.name(),
                basicInfoDto.toCategory(),
                basicInfoDto.phoneNumber(),
                basicInfoDto.description()
        );
    }

    @Transactional
    public void updateStoreOperationInfo(Long storeId, StoreUpdateOperationInfoRequestDto dto, Long userId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        checkStoreOwnerPermission(store, userId);

        // TODO: 서비스 레이어에서 dto의 값을 직접 풀어서 entity로 전달하는 것은 좋지 않은 방법이다
        // 추후 MapStruct 등의 방법을 사용해 대체할 예정
        RequestStoreOperationInfo.ParsedOperationInfo parsedData = dto.operationInfo().toParsedData();
        store.updateOperationInfo(
                parsedData.getClosedDays(),
                parsedData.getWeekdayWorkingTime().getFirst(),
                parsedData.getWeekdayWorkingTime().getSecond(),
                parsedData.getWeekdayBreakTime().getFirst(),
                parsedData.getWeekdayBreakTime().getSecond(),
                parsedData.getWeekendWorkingTime().getFirst(),
                parsedData.getWeekendWorkingTime().getSecond(),
                parsedData.getWeekendBreakTime().getFirst(),
                parsedData.getWeekendBreakTime().getSecond()
        );
    }

    @Transactional
    public void updateStoreOrderSettings(Long storeId, StoreUpdateOrderSettingsRequestDto dto, Long userId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        checkStoreOwnerPermission(store, userId);

        RequestStoreOrderSettings orderSettingsInfo = dto.orderSettingsInfo();
        store.updateOrderSettings(
                orderSettingsInfo.minDeliveryPrice(),
                orderSettingsInfo.deliveryTip()
        );
    }

    @Transactional
    public void updateTemporarilyClosed(Long storeId, StoreUpdateStatusRequest dto, Long userId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        checkStoreOwnerPermission(store, userId);

        store.updateTemporarilyClosed(dto.status());
        StoreStatus storeStatus = storeStatusResolveService.resolveStoreStatus(store, LocalDateTime.now());
        store.updateStatus(storeStatus);
    }

    @Transactional
    public void updatePermanentlyClosed(Long storeId, StoreUpdateStatusRequest dto, Long userId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        checkStoreOwnerPermission(store, userId);

        // 폐업 상태의 가게를 활성화하려는데 최대 운영 가게 개수 제한에 걸린다면 예외 발생
        if (!dto.status() && !storeRepository.isStoreRegistrationAvailable(userId)) {
            throw new BusinessException(ResultCode.STORE_LIMIT_EXCEEDED);
        }

        store.updatePermanentlyClosed(dto.status());
        StoreStatus storeStatus = storeStatusResolveService.resolveStoreStatus(store, LocalDateTime.now());
        store.updateStatus(storeStatus);
    }

    private void checkStoreOwnerPermission(Store store, Long userId) {
        if (!Objects.equals(store.getUser().getId(), userId)) {
            throw new BusinessException(ResultCode.STORE_FORBIDDEN_ACCESS);
        }
    }

}

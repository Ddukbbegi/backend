package com.ddukbbegi.api.store.service;

import com.ddukbbegi.api.common.dto.PageResponseDto;
import com.ddukbbegi.api.store.dto.request.*;
import com.ddukbbegi.api.store.dto.response.*;
import com.ddukbbegi.api.store.entity.Store;
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

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public StoreIdResponseDto registerStore(StoreRegisterRequestDto dto, Long userId) {

        User user = userRepository.findByIdOrElseThrow(userId);
        if (!storeRepository.isStoreRegistrationAvailable(userId)) {
            throw new BusinessException(ResultCode.STORE_LIMIT_EXCEEDED);
        }

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

        Page<StorePageItemResponseDto> result = storeRepository.findAllOpenedStoreByName("%" + name + "%", pageable)
                .map(StorePageItemResponseDto::fromEntity);
        return PageResponseDto.toDto(result);
    }

    @Transactional(readOnly = true)
    public StoreDetailResponseDto getStore(Long storeId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        return StoreDetailResponseDto.fromEntity(store);
    }

    @Transactional
    public void updateStoreBasicInfo(Long storeId, StoreUpdateBasicInfoRequestDto dto) {

        // TODO: 서비스 레이어에서 dto의 값을 직접 풀어서 entity로 전달하는 것은 좋지 않은 방법이다
        // 추후 MapStruct 등의 방법을 사용해 대체할 예정
        StoreBasicInfoDto basicInfoDto = dto.basicInfoDto();

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        store.updateBasicInfo(
                basicInfoDto.name(),
                basicInfoDto.getCategory(),
                basicInfoDto.phoneNumber(),
                basicInfoDto.description()
        );
    }

    @Transactional
    public void updateStoreOperationInfo(Long storeId, StoreUpdateOperationInfoRequestDto dto) {

        StoreOperationInfoDto.ParsedOperationInfo parsedData = dto.operationInfo().toParsedData();

        Store store = storeRepository.findByIdOrElseThrow(storeId);
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
    public void updateStoreOrderSettings(Long storeId, StoreUpdateOrderSettingsRequestDto dto) {

        StoreOrderSettingsInfo orderSettingsInfo = dto.orderSettingsInfo();

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        store.updateOrderSettings(
                orderSettingsInfo.minDeliveryPrice(),
                orderSettingsInfo.deliveryTip()
        );
    }

    public void updateTemporarilyClosed(Long storeId, StoreUpdateStatusRequest dto) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        store.updateTemporarilyClosed(dto.status());
    }

    public void updatePermanentlyClosed(Long storeId, StoreUpdateStatusRequest dto) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        store.updatePermanentlyClosed(dto.status());
    }
}

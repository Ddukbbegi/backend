package com.ddukbbegi.api.store.service;

import com.ddukbbegi.api.common.dto.PageResponseDto;
import com.ddukbbegi.api.store.dto.request.*;
import com.ddukbbegi.api.store.dto.response.OwnerStoreResponseDto;
import com.ddukbbegi.api.store.dto.response.StoreIdResponseDto;
import com.ddukbbegi.api.store.dto.response.StorePageItemResponseDto;
import com.ddukbbegi.api.store.dto.response.StoreRegisterAvailableResponseDto;
import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.StoreCategory;
import com.ddukbbegi.api.store.repository.StoreRepository;
import com.ddukbbegi.api.store.util.TimeRangeParser;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.common.component.ResultCode;
import com.ddukbbegi.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public StoreIdResponseDto registerStore(StoreRegisterRequestDto dto) {

        if (!storeRepository.isStoreRegistrationAvailable(1L)) {    // TODO: user 연동
            throw new BusinessException(ResultCode.STORE_LIMIT_EXCEEDED);
        }

        User user = userRepository.findByIdOrElseThrow(1L); // TODO: user 연동
        Store entity = dto.toEntity(user);
        Store savedStore = storeRepository.save(entity);

        return StoreIdResponseDto.of(savedStore.getId());
    }

    @Transactional(readOnly = true)
    public StoreRegisterAvailableResponseDto checkStoreRegistrationAvailability() {

        boolean available = storeRepository.isStoreRegistrationAvailable(1L); // TODO: user 연동
        return StoreRegisterAvailableResponseDto.of(available);
    }

    @Transactional(readOnly = true)
    public List<OwnerStoreResponseDto> getOwnerStoreList() {

        List<Store> storeList = storeRepository.findAllByUser_Id(1L);   // TODO: user 연동
        return storeList.stream()
                .map(OwnerStoreResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponseDto<StorePageItemResponseDto> getStores(String name, Pageable pageable) {

        Page<StorePageItemResponseDto> result = storeRepository.findAllOpenedStoreByName("%" + name + "%", pageable)
                .map(StorePageItemResponseDto::fromEntity);
        return PageResponseDto.toDto(result);
    }

    @Transactional
    public void updateStoreBasicInfo(Long storeId, StoreUpdateBasicInfoRequestDto dto) {

        // TODO: 서비스 레이어에서 dto의 값을 직접 풀어서 entity로 전달하는 것은 좋지 않은 방법이다
        // 추후 MapStruct 등의 방법을 사용해 대체할 예정
        Store store = storeRepository.findByIdOrElseThrow(storeId);
        store.updateBasicInfo(
                dto.getName(),
                StoreCategory.fromString(dto.getCategory()),
                dto.getPhoneNumber(),
                dto.getDescription()
        );
    }

    @Transactional
    public void updateStoreOperationInfo(Long storeId, StoreUpdateOperationInfoRequestDto dto) {

        Pair<LocalTime, LocalTime> weekdayWorkingTime = TimeRangeParser.parse(dto.getWeekdayWorkingTime());
        Pair<LocalTime, LocalTime> weekdayBreakTime = TimeRangeParser.parse(dto.getWeekdayBreakTime());
        Pair<LocalTime, LocalTime> weekendWorkingTime = TimeRangeParser.parse(dto.getWeekendWorkingTime());
        Pair<LocalTime, LocalTime> weekendBreakTime = TimeRangeParser.parse(dto.getWeekendBreakTime());

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        store.updateOperationInfo(
                weekdayWorkingTime.getFirst(),
                weekdayWorkingTime.getSecond(),
                weekdayBreakTime.getFirst(),
                weekdayBreakTime.getSecond(),
                weekendWorkingTime.getFirst(),
                weekendWorkingTime.getSecond(),
                weekendBreakTime.getFirst(),
                weekendBreakTime.getSecond()
        );
    }

    @Transactional
    public void updateStoreOrderSettings(Long storeId, StoreUpdateOrderSettingsRequestDto dto) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        store.updateOrderSettings(
                dto.getMinDeliveryPrice(),
                dto.getDeliveryTip()
        );
    }

    public void updateTemporarilyClosed(Long storeId, StoreUpdateStatusRequest dto) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        store.updateTemporarilyClosed(dto.isStatus());
    }

    public void updatePermanentlyClosed(Long storeId, StoreUpdateStatusRequest dto) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        store.updatePermanentlyClosed(dto.isStatus());
    }
}

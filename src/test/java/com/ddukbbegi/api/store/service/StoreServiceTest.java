package com.ddukbbegi.api.store.service;

import com.ddukbbegi.api.common.dto.PageResponseDto;
import com.ddukbbegi.api.store.dto.request.*;
import com.ddukbbegi.api.store.dto.response.*;
import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.StoreCategory;
import com.ddukbbegi.api.store.enums.StoreStatus;
import com.ddukbbegi.api.store.repository.StoreRepository;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.common.component.ResultCode;
import com.ddukbbegi.common.exception.BusinessException;
import com.ddukbbegi.support.fixture.StoreFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @InjectMocks private StoreService storeService;

    @Mock private StoreRepository storeRepository;
    @Mock private UserRepository userRepository;

    @DisplayName("가게 등록 테스트")
    @Nested
    class RegisterStoreTest {

        @DisplayName("성공 - 활성화된 가게가 3개 미만일 경우 가게 등록 성공")
        @Test
        void givenRegisterDtoAndUserId_whenSave_thenWorksFine() {
            // given
            Long userId = 1L;
            User user = StoreFixture.createUser();
            StoreRegisterRequestDto dto = StoreFixture.createStoreRegisterRequestDto();
            Store store = dto.toEntity(user);
            ReflectionTestUtils.setField(store, "id", 1L);

            given(storeRepository.isStoreRegistrationAvailable(userId)).willReturn(true);
            given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);
            given(storeRepository.save(any(Store.class))).willReturn(store);

            // when
            StoreIdResponseDto storeIdResponseDto = storeService.registerStore(dto, userId);

            // then
            assertThat(storeIdResponseDto.storeId()).isEqualTo(1L);
        }

        @DisplayName("실패 - 이미 등록된 가게가 3개 이상일 경우 예외 발생")
        @Test
        void givenRegistrationUnavailable_whenSave_thenThrowsException() {
            // given
            Long userId = 1L;
            StoreRegisterRequestDto dto = StoreFixture.createStoreRegisterRequestDto();

            given(storeRepository.isStoreRegistrationAvailable(userId)).willReturn(false);

            // when & then
            assertThatThrownBy(() -> storeService.registerStore(dto, userId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ResultCode.STORE_LIMIT_EXCEEDED.getDefaultMessage());
        }

    }

    @DisplayName("성공 - 가게 추가 등록 가능 여부 확인")
    @Test
    void givenUserId_whenCheckRegistration_thenReturnRegistrationAvailable() {
        // given
        Long userId = 1L;

        given(storeRepository.isStoreRegistrationAvailable(userId)).willReturn(true);

        // when
        StoreRegisterAvailableResponseDto response = storeService.checkStoreRegistrationAvailability(userId);

        // then
        assertThat(response.isAvailable()).isTrue();
    }

    @DisplayName("성공 - 사장님 소유 가게 목록 조회")
    @Test
    void givenUserId_whenGetOwnerStoreList_thenReturnStoreList() {
        // given
        long userId = 1L;
        Store store = StoreFixture.createStore(mock(User.class));

        given(storeRepository.findAllByUser_Id(userId)).willReturn(List.of(store));

        // when
        List<OwnerStoreResponseDto> result = storeService.getOwnerStoreList(userId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).category()).isEqualTo("한식");
    }

    @DisplayName("사장님 소유 가게 상세 조회")
    @Nested
    class GetOwnerStoreDetailTest {

        @DisplayName("성공 - 가게 상세 조회 (소유주 일치)")
        @Test
        void givenStoreIdAndUserId_whenGetOwnerStoreDetail_thenReturnStoreDetail() {
            // given
            Long userId = 1L;
            Long storeId = 2L;

            User user = mock(User.class);
            Store store = StoreFixture.createStore(user);

            ReflectionTestUtils.setField(store, "user", user);
            ReflectionTestUtils.setField(user, "id", userId);

            given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);
            given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(store);
            given(user.getId()).willReturn(1L);

            // when
            OwnerStoreDetailResponseDto response = storeService.getOwnerStoreDetail(2L, 1L);

            // then
            assertThat(response).isNotNull();
            assertThat(response.basicInfo().category()).isEqualTo("한식");
            assertThat(response.status()).isEqualTo(StoreStatus.CLOSED);
        }

        @DisplayName("실패 - 소유주 불일치로 접근 거부")
        @Test
        void givenWrongUserId_whenGetOwnerStoreDetail_thenThrowException() {
            // given
            Long wrongUserId = 99L;
            User owner = mock(User.class);
            User otherUser = mock(User.class);
            Store store = StoreFixture.createStore(owner);

            ReflectionTestUtils.setField(store, "user", owner);

            given(userRepository.findByIdOrElseThrow(wrongUserId)).willReturn(otherUser);
            given(storeRepository.findByIdOrElseThrow(2L)).willReturn(store);
            given(owner.getId()).willReturn(1L);
            given(otherUser.getId()).willReturn(wrongUserId);

            // when & then
            assertThatThrownBy(() -> storeService.getOwnerStoreDetail(2L, wrongUserId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ResultCode.STORE_FORBIDDEN_ACCESS.getDefaultMessage());
        }
    }

    @DisplayName("성공 - 이름으로 가게 리스트 조회")
    @Test
    void givenNameAndPageable_whenGetStores_thenReturnPageResponse() {
        // given
        Store store = StoreFixture.createStore(mock(User.class));
        PageRequest pageable = PageRequest.of(0, 10);

        given(storeRepository.findAllStoreByName(any(), eq(pageable)))
                .willReturn(new PageImpl<>(List.of(store)));

        // when
        PageResponseDto<StorePageItemResponseDto> result = storeService.getStores("김밥", pageable);

        // then
        List<StorePageItemResponseDto> data = result.getData();
        assertThat(data).hasSize(1);
        assertThat(data.get(0).name()).isEqualTo("맛있는김밥");
        assertThat(data.get(0).storeCategory()).isEqualTo("한식");
    }

    @DisplayName("성공 - 가게 상세 조회")
    @Test
    void givenStoreId_whenGetStore_thenReturnStoreDetail() {
        // given
        Long userId = 1L;
        Store store = StoreFixture.createStore(mock(User.class));

        given(storeRepository.findByIdOrElseThrow(userId)).willReturn(store);

        // when
        StoreDetailResponseDto result = storeService.getStore(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.basicInfo().category()).isEqualTo("한식");
    }

    @DisplayName("가게 기본 정보 업데이트 테스트")
    @Nested
    class UpdateStoreBasicInfoTest {

        @DisplayName("성공 - 기본 정보 업데이트")
        @Test
        void givenDto_whenUpdateStoreBasicInfo_thenUpdateSuccess() {
            // given
            User user = mock(User.class);
            Store store = StoreFixture.createStore(user);
            StoreUpdateBasicInfoRequestDto dto = new StoreUpdateBasicInfoRequestDto(StoreFixture.createBasicInfo());

            ReflectionTestUtils.setField(store, "user", user);
            ReflectionTestUtils.setField(store, "name", "청화성");
            ReflectionTestUtils.setField(store, "category", StoreCategory.CHINESE);

            given(storeRepository.findByIdOrElseThrow(1L)).willReturn(store);
            given(user.getId()).willReturn(1L);

            // when
            storeService.updateStoreBasicInfo(1L, dto, 1L);

            // then
            assertThat(store.getName()).isEqualTo("맛있는김밥");
            assertThat(store.getCategory()).isEqualTo(StoreCategory.KOREAN);
        }

        @DisplayName("실패 - 가게 소유주 불일치로 접근 거부")
        @Test
        void givenWrongUserId_whenCheckOwnerPermission_thenThrowsException() {
            // given
            Long storeId = 1L;
            Long ownerId = 1L;
            Long otherId = 99L;
            User owner = mock(User.class);
            Store store = mock(Store.class);
            StoreUpdateBasicInfoRequestDto dto = new StoreUpdateBasicInfoRequestDto(StoreFixture.createBasicInfo());

            given(storeRepository.findByIdOrElseThrow(1L)).willReturn(store);
            given(store.getUser()).willReturn(owner);
            given(owner.getId()).willReturn(ownerId);

            // when & then
            assertThatThrownBy(() -> storeService.updateStoreBasicInfo(storeId, dto, otherId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ResultCode.STORE_FORBIDDEN_ACCESS.getDefaultMessage());
        }

    }

    @DisplayName("가게 폐업 테스트")
    @Nested
    class UpdatePermanentlyClosedTest {

        @DisplayName("성공 - 가게의 폐업 상태를 해제하려고 할 때 활성화된 가게가 3개 미만이면 성공")
        @Test
        void givenPermanentlyClosedIsFalse_whenUpdateStatus_thenSuccess() {
            // given
            Long storeId = 1L;
            Long userId = 2L;

            StoreUpdateStatusRequest dto = new StoreUpdateStatusRequest(false);
            User user = mock(User.class);
            Store store = StoreFixture.createStore(user);
            
            ReflectionTestUtils.setField(store, "user", user);
            ReflectionTestUtils.setField(store, "isPermanentlyClosed", true); // 값 바뀌는지 확인하기 위해 설정

            given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(store);
            given(user.getId()).willReturn(userId);
            given(storeRepository.isStoreRegistrationAvailable(userId)).willReturn(true);

            // when
            storeService.updatePermanentlyClosed(storeId, dto, userId);

            // then
            assertThat(store.isPermanentlyClosed()).isFalse();
        }

        @DisplayName("실패 - 폐업 상태의 가게를 활성화하려는데 이미 활성화된 가게가 3개 이상이면 예외 발생")
        @Test
        void givenFalseStatusAndRegistrationUnavailable_whenUpdateStatus_thenThrowsException() {
            // given
            Long storeId = 1L;
            Long userId = 2L;

            StoreUpdateStatusRequest dto = new StoreUpdateStatusRequest(false);
            User user = mock(User.class);
            Store store = StoreFixture.createStore(user);

            ReflectionTestUtils.setField(store, "user", user);

            given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(store);
            given(user.getId()).willReturn(userId);
            given(storeRepository.isStoreRegistrationAvailable(userId)).willReturn(false);

            // when & then
            assertThatThrownBy(() -> storeService.updatePermanentlyClosed(storeId, dto, userId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ResultCode.STORE_LIMIT_EXCEEDED.getDefaultMessage());
        }

    }

}

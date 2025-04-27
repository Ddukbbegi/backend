package com.ddukbbegi.api.store.repository;

import com.ddukbbegi.api.store.dto.request.StoreRegisterRequestDto;
import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.StoreStatus;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.support.config.TestAuditorConfig;
import com.ddukbbegi.support.fixture.StoreFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestAuditorConfig.class)
@ActiveProfiles("test")
class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    private User userEntity;
    private StoreRegisterRequestDto dto;

    @BeforeEach
    void setup() {
        userEntity = StoreFixture.createUser();
        dto = StoreFixture.createStoreRegisterRequestDto();
    }

    @DisplayName("가게 검색 목록 조회 테스트")
    @Nested
    class FindAllOpenedStoreByNameTest {

        @DisplayName("성공 - 가게 이름 검색 +_폐업 가게 제외 + OPEN 가게 우선 정렬")
        @Test
        void givenStoreName_whenFindAllStoreByName_thenReturnPagedStores() {
            // given
            Pageable pageable = PageRequest.of(0, 10);

            User savedUser = userRepository.save(userEntity);

            Store store1 = dto.toEntity(savedUser);
            Store store2 = dto.toEntity(savedUser);
            Store store3 = dto.toEntity(savedUser);

            store1.updateStatus(StoreStatus.PERMANENTLY_CLOSED);
            store2.updateStatus(StoreStatus.TEMPORARILY_CLOSED);
            store3.updateStatus(StoreStatus.OPEN);

            storeRepository.save(store1);
            storeRepository.save(store2);
            storeRepository.save(store3);

            // when
            Page<Store> result = storeRepository.findAllStoreByName("%김밥%", pageable);

            // then
            List<Store> stores = result.getContent();

            assertThat(stores.get(0).getStatus()).isEqualTo(StoreStatus.OPEN);
            assertThat(stores.get(1).getStatus()).isEqualTo(StoreStatus.TEMPORARILY_CLOSED);
            assertThat(stores).noneMatch(s -> s.getStatus() == StoreStatus.PERMANENTLY_CLOSED);
        }

    }

    @DisplayName("가게 추가 등록 가능 여부 조회 테스트")
    @Nested
    class IsStoreRegistrationAvailableTest {

        @DisplayName("성공 - 운영중 가게 수가 3개 미만이면 등록 가능")
        @Test
        void givenLessThanThreeStores_whenCheckAvailability_thenReturnTrue() {
            // given
            User savedUser = userRepository.save(userEntity);

            Store store1 = dto.toEntity(savedUser);
            Store store2 = dto.toEntity(savedUser);
            Store store3 = dto.toEntity(savedUser);

            // 등록된 가게는 3개지만 활성화된 가게는 2개 (폐업 상태인 가게는 제외)
            store1.updatePermanentlyClosed(true);

            storeRepository.save(store1);
            storeRepository.save(store2);
            storeRepository.save(store3);

            // when
            boolean available = storeRepository.isStoreRegistrationAvailable(savedUser.getId());

            // then
            assertThat(available).isTrue();
        }

        @DisplayName("성공 - 운영중 가게 수가 3개 이상이면 등록 불가")
        @Test
        void givenThreeOrMoreStores_whenCheckAvailability_thenReturnFalse() {
            // given
            User savedUser = userRepository.save(userEntity);

            Store store1 = dto.toEntity(savedUser);
            Store store2 = dto.toEntity(savedUser);
            Store store3 = dto.toEntity(savedUser);

            storeRepository.save(store1);
            storeRepository.save(store2);
            storeRepository.save(store3);

            // when
            boolean available = storeRepository.isStoreRegistrationAvailable(savedUser.getId());

            // then
            assertThat(available).isFalse();
        }

    }

}

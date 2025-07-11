package com.ddukbbegi.support.fixture;

import com.ddukbbegi.api.store.dto.request.RequestStoreBasicInfo;
import com.ddukbbegi.api.store.dto.request.RequestStoreOperationInfo;
import com.ddukbbegi.api.store.dto.request.RequestStoreOrderSettings;
import com.ddukbbegi.api.store.dto.request.StoreRegisterRequestDto;
import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.enums.UserRole;

/**
 * Store 도메인 관련 테스트 코드 작성 시 공통으로 사용되는
 * 테스트 더미 데이터 생성 클래스
 */
public class StoreFixture {

    public static Store createStore(User user) {
        return new StoreRegisterRequestDto(
                createBasicInfo(),
                createOperationInfo(),
                createOrderSettings()
        ).toEntity(user);
    }

    public static StoreRegisterRequestDto createStoreRegisterRequestDto() {
        return new StoreRegisterRequestDto(
                createBasicInfo(),
                createOperationInfo(),
                createOrderSettings()
        );

    }
    // 연중무휴 24시간 운영하는 가게. 항상 OPEN인 상태를 유지함
    public static StoreRegisterRequestDto create24StoreRegisterRequestDto() {
        return new StoreRegisterRequestDto(
                createBasicInfo(),
                create24OperationInfo(),
                createOrderSettings()
        );
    }

    public static User createUser() {
        return User.of(
                "test@email.com",
                "pw",
                "사장님",
                "010-1234-5678",
                UserRole.OWNER
        );
    }

    public static RequestStoreBasicInfo createBasicInfo() {
        return new RequestStoreBasicInfo(
                "맛있는김밥",
                "한식",
                "010-1234-5678",
                "신선한 재료로 매일 준비합니다."
        );
    }

    public static RequestStoreOperationInfo createOperationInfo() {
        return new RequestStoreOperationInfo(
                "SUN,MON",
                "10:00-18:00",
                "13:00-14:00",
                "11:00-17:00",
                "12:00-13:00"
        );
    }

    public static RequestStoreOperationInfo create24OperationInfo() {
        return new RequestStoreOperationInfo(
                "",
                "00:00-00:00",
                "00:00-00:00",
                "00:00-00:00",
                "00:00-00:00"
        );
    }

    public static RequestStoreOrderSettings createOrderSettings() {
        return new RequestStoreOrderSettings(
                10000,
                3000
        );
    }

}

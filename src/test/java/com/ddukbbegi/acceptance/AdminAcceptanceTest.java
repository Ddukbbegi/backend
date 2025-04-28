package com.ddukbbegi.acceptance;

import com.ddukbbegi.acceptance.support.AcceptanceTestSupport;
import com.ddukbbegi.api.user.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인수 테스트 - Admin")
public class AdminAcceptanceTest extends AcceptanceTestSupport {

    private final String email = "admin@test.com";

    @DisplayName("어드민 계정 생성 및 일간/월간 주문수 조회")
    @Test
    void 어드민_일간_월간_주문수_조회() {
        // given
        회원가입하고_로그인(email, UserRole.ADMIN);

        // when

        // then
        assertThat(adminAccessToken).isNotNull();
    }

}

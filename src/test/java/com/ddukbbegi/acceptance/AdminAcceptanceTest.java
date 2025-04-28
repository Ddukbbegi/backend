package com.ddukbbegi.acceptance;

import com.ddukbbegi.acceptance.support.AcceptanceTestSupport;
import com.ddukbbegi.api.order.enums.OrderStatus;
import com.ddukbbegi.api.store.dto.response.StorePageItemResponseDto;
import com.ddukbbegi.api.user.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인수 테스트 - Admin")
public class AdminAcceptanceTest extends AcceptanceTestSupport {

    @DisplayName("1. (어드민) 일간/월간 주문수를 조회한다.")
    @Test
    void 어드민_일간_월간_주문수_조회() {
        // given
        회원가입하고_로그인("owner51@test.com", UserRole.OWNER);
        Long storeId = 사장_24시운영_가게등록();
        Long menuId1 = 사장_메뉴등록(storeId, "참치김밥");
        Long menuId2 = 사장_메뉴등록(storeId, "치즈김밥");

        회원가입하고_로그인("user51@test.com", UserRole.USER);
        Long orderId1 = 유저_주문요청(List.of(menuId1, menuId2));
        Long orderId2 = 유저_주문요청(List.of(menuId1, menuId2));

        사장_주문수락(orderId1);
        사장_주문수락(orderId2);

        회원가입하고_로그인("admin@test.com", UserRole.ADMIN);

        // when
        Long 일간주문수 = 어드민_일간주문수_조회();
        Long 월간주문수 = 어드민_월간주문수_조회();

        // then
        assertThat(일간주문수).isEqualTo(2);
        assertThat(월간주문수).isEqualTo(2);
    }

    private Long 어드민_일간주문수_조회() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return given()
                .header("Authorization", adminAccessToken)
                .queryParam("date", today.format(formatter))
            .when()
                .get("/api/admin/orderCount/date")
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("data.totalOrder");
    }

    private Long 어드민_월간주문수_조회() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return given()
                .header("Authorization", adminAccessToken)
                .queryParam("month", today.format(formatter))
            .when()
                .get("/api/admin/orderCount/month")
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("data.totalOrder");
    }

}

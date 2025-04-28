package com.ddukbbegi.acceptance;

import com.ddukbbegi.acceptance.support.AcceptanceTestSupport;
import com.ddukbbegi.api.order.dto.response.OrderHistoryOwnerResponseDto;
import com.ddukbbegi.api.order.entity.Order;
import com.ddukbbegi.api.order.enums.OrderStatus;
import com.ddukbbegi.api.user.enums.UserRole;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Order 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTestSupport {

    @DisplayName("1. (사장) 메뉴 등록 후 주문 요청이 들어오면 내용을 확인하고 수락한다.")
    @Test
    void givenOrdersThenAcceptThem() {
        // given
        회원가입하고_로그인("owner31@test.com", UserRole.OWNER);
        Long storeId = 사장_24시운영_가게등록();
        Long menuId1 = 사장_메뉴등록(storeId, "참치김밥");
        Long menuId2 = 사장_메뉴등록(storeId, "치즈김밥");

        회원가입하고_로그인("user31@test.com", UserRole.USER);
        Long orderId = 유저_주문요청(List.of(menuId1, menuId2));

        List<OrderHistoryOwnerResponseDto> 사장_주문내역 = 사장_주문내역조회(storeId);

        // when
        사장_주문수락(orderId);

        // then
        assertThat(사장_주문내역.size()).isEqualTo(1);

        Page<Order> orders = orderRepository.findAllByStoreId(storeId, PageRequest.of(0, 10));
        Order order = orders.getContent().get(0);
        assertThat(orders.getContent().size()).isEqualTo(1);
        assertThat(order.getId()).isEqualTo(orderId);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ACCEPTED);
    }

    @DisplayName("2. (유저) 주문을 요청했다가 취소한다.")
    @Test
    void createOrderAndCancelIt() {
        // given
        회원가입하고_로그인("owner32@test.com", UserRole.OWNER);
        Long storeId = 사장_24시운영_가게등록();
        Long menuId1 = 사장_메뉴등록(storeId, "참치김밥");
        Long menuId2 = 사장_메뉴등록(storeId, "치즈김밥");

        회원가입하고_로그인("user32@test.com", UserRole.USER);
        Long orderId = 유저_주문요청(List.of(menuId1, menuId2));

        // when
        유저_주문취소(orderId);

        // then
        Page<Order> orders = orderRepository.findAllByStoreId(storeId, PageRequest.of(0, 10));
        Order order = orders.getContent().get(0);
        assertThat(orders.getContent().size()).isEqualTo(1);
        assertThat(order.getId()).isEqualTo(orderId);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    private List<OrderHistoryOwnerResponseDto> 사장_주문내역조회(Long storeId) {
        return given()
                .header("Authorization", ownerAccessToken)
            .when()
                .get("/api/owner/stores/{storeId}/orders", storeId)
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.data", OrderHistoryOwnerResponseDto.class);
    }

    private void 사장_주문수락(Long orderId) {
        given()
                .header("Authorization", ownerAccessToken)
                .contentType(ContentType.JSON)
                .queryParam("status", "ACCEPTED")
            .when()
                .patch("/api/owner/orders/{orderId}", orderId)
            .then()
                .statusCode(200);
    }

    private void 유저_주문취소(Long orderId) {
        given()
                .header("Authorization", userAccessToken)
                .contentType(ContentType.JSON)
                .queryParam("status", "ACCEPTED")
            .when()
                .patch("/api/orders/{orderId}/cancel", orderId)
            .then()
                .statusCode(200);
    }

}

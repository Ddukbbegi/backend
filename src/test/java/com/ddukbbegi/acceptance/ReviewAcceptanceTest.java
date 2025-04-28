package com.ddukbbegi.acceptance;

import com.ddukbbegi.acceptance.support.AcceptanceTestSupport;
import com.ddukbbegi.api.order.entity.Order;
import com.ddukbbegi.api.order.enums.OrderStatus;
import com.ddukbbegi.api.review.dto.ReviewOwnerRequestDto;
import com.ddukbbegi.api.review.entity.Review;
import com.ddukbbegi.api.user.enums.UserRole;
import com.ddukbbegi.support.fixture.ReviewFixture;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인수 테스트 - Review")
public class ReviewAcceptanceTest extends AcceptanceTestSupport {

    @DisplayName("1. (유저) 주문 후 리뷰를 등록한다.")
    @Test
    void doOrderThenWriteReview() {
        // given
        회원가입하고_로그인("owner41@test.com", UserRole.OWNER);
        Long storeId = 사장_24시운영_가게등록();
        Long menuId1 = 사장_메뉴등록(storeId, "참치김밥");
        Long menuId2 = 사장_메뉴등록(storeId, "치즈김밥");

        회원가입하고_로그인("user41@test.com", UserRole.USER);
        Long orderId = 유저_주문요청(List.of(menuId1, menuId2));

        사장_주문수락(orderId);
        사장_주문상태변경(orderId, OrderStatus.COOKING);        // 요리 중
        사장_주문상태변경(orderId, OrderStatus.DELIVERING);     // 배달 중
        사장_주문상태변경(orderId, OrderStatus.DELIVERED);      // 배달 완료

        // when
        Long reviewId = 유저_리뷰작성(orderId);

        // then
        Order order = orderRepository.findByIdOrElseThrow(orderId);
        Review review = reviewRepository.findByIdOrElseThrow(reviewId);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.DELIVERED);
        assertThat(review.getContents()).isEqualTo("잘 먹었습니다.");
    }

    @DisplayName("2. (사장) 자신의 가게에 달린 리뷰에 답글을 작성한다.")
    @Test
    void replyReview() {
        // given
        회원가입하고_로그인("owner42@test.com", UserRole.OWNER);
        Long storeId = 사장_24시운영_가게등록();
        Long menuId1 = 사장_메뉴등록(storeId, "참치김밥");
        Long menuId2 = 사장_메뉴등록(storeId, "치즈김밥");

        회원가입하고_로그인("user42@test.com", UserRole.USER);
        Long orderId = 유저_주문요청(List.of(menuId1, menuId2));

        사장_주문수락(orderId);
        사장_주문상태변경(orderId, OrderStatus.COOKING);        // 요리 중
        사장_주문상태변경(orderId, OrderStatus.DELIVERING);     // 배달 중
        사장_주문상태변경(orderId, OrderStatus.DELIVERED);      // 배달 완료

        Long reviewId = 유저_리뷰작성(orderId);

        // when
        사장_리뷰답글(reviewId);

        // then
        Review review = reviewRepository.findByIdOrElseThrow(reviewId);
        assertThat(review.getReply()).isEqualTo("감사합니다!");
    }

    private void 사장_주문상태변경(Long orderId, OrderStatus orderStatus) {
        given()
                .header("Authorization", ownerAccessToken)
                .contentType(ContentType.JSON)
                .queryParam("status", orderStatus.name())
            .when()
                .patch("/api/owner/orders/{orderId}", orderId)
            .then()
                .statusCode(200);
    }

    private Long 유저_리뷰작성(Long orderId) {
        return given()
                .header("Authorization", userAccessToken)
                .contentType(ContentType.JSON)
                .body(ReviewFixture.createReviewRequestDto(orderId, "잘 먹었습니다."))
            .when()
                .post("/api/reviews")
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("data.reviewId");
    }

    private void 사장_리뷰답글(Long reviewId) {
        given()
                .header("Authorization", ownerAccessToken)
                .contentType(ContentType.JSON)
                .body(new ReviewOwnerRequestDto("감사합니다!"))
            .when()
                .post("/api/owners/reviews/{reviewId}/reply", reviewId)
            .then()
                .statusCode(200);
    }
    
}

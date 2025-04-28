package com.ddukbbegi.acceptance.support;

import com.ddukbbegi.api.auth.dto.request.LoginRequestDto;
import com.ddukbbegi.api.auth.dto.request.SignupRequestDto;
import com.ddukbbegi.api.menu.repository.MenuRepository;
import com.ddukbbegi.api.order.repository.OrderRepository;
import com.ddukbbegi.api.review.repository.ReviewRepository;
import com.ddukbbegi.api.store.repository.StoreRepository;
import com.ddukbbegi.api.user.enums.UserRole;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.support.config.TestContainersConfig;
import com.ddukbbegi.support.fixture.MenuFixture;
import com.ddukbbegi.support.fixture.OrderFixture;
import com.ddukbbegi.support.fixture.StoreFixture;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * 인수 테스트 시 필요한 설정과 공통 메서드를 모아둔 추상 클래스
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTestSupport extends TestContainersConfig {

    @Autowired protected UserRepository userRepository;
    @Autowired protected StoreRepository storeRepository;
    @Autowired protected MenuRepository menuRepository;
    @Autowired protected OrderRepository orderRepository;
    @Autowired protected ReviewRepository reviewRepository;

    @Autowired private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    protected int port;

    protected String adminAccessToken;
    protected String ownerAccessToken;
    protected String userAccessToken;

    @BeforeEach
    void cleanUp() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

        // DB에 있는 모든 테이블을 동적으로 조회해서 TRUNCATE
        jdbcTemplate.queryForList("SHOW TABLES", String.class)
                .forEach(table -> {
                    jdbcTemplate.execute("TRUNCATE TABLE " + table);
                });

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }

    @BeforeEach
    void setUpRestAssuredPort() {
        RestAssured.port = port;
    }

    protected void 회원가입하고_로그인(String email, UserRole userRole) {
        회원가입(email, userRole);
        String token = 로그인_및_액세스_토큰_발급(email, userRole);
        switch (userRole) {
            case ADMIN -> adminAccessToken = token;
            case OWNER -> ownerAccessToken = token;
            case USER -> userAccessToken = token;
        }
    }

    protected void 회원가입(String email, UserRole userRole) {
        given()
                .contentType(ContentType.JSON)
                .body(new SignupRequestDto(email, "Pwdpwdpwd1!", "test", "010-1234-5678", userRole.name()))
            .when()
                .post("/api/auth/signup")
            .then()
                .statusCode(200);
    }

    protected String 로그인_및_액세스_토큰_발급(String email, UserRole userRole) {
        return given()
                .contentType(ContentType.JSON)
                .body(new LoginRequestDto(email, "Pwdpwdpwd1!"))
            .when()
                .post("/api/auth/login")
            .then()
                .statusCode(200)
                .extract()
                .path("data.accessToken");
    }

    protected Long 사장_가게등록() {
        return given()
                .header("Authorization", ownerAccessToken)
                .contentType(ContentType.JSON)
                .body(StoreFixture.createStoreRegisterRequestDto())
            .when()
                .post("/api/owner/stores")
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("data.storeId");
    }

    protected Long 사장_24시운영_가게등록() {
        return given()
                .header("Authorization", ownerAccessToken)
                .contentType(ContentType.JSON)
                .body(StoreFixture.create24StoreRegisterRequestDto())
            .when()
                .post("/api/owner/stores")
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("data.storeId");
    }

    protected Long 사장_메뉴등록(Long storeId, String menuName) {
        return given()
                .header("Authorization", ownerAccessToken)
                .contentType(ContentType.JSON)
                .body(MenuFixture.createNewMenuRequestDto(menuName))
            .when()
                .post("/api/owner/stores/{storeId}/menus", storeId)
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("data");
    }

    protected Long 유저_주문요청(List<Long> menuIds) {
        return given()
                .header("Authorization", userAccessToken)
                .contentType(ContentType.JSON)
                .body(OrderFixture.createOrderCreateRequestDto(menuIds))
            .when()
                .post("/api/orders")
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("data.id");
    }

    protected void 사장_주문수락(Long orderId) {
        given()
                .header("Authorization", ownerAccessToken)
                .contentType(ContentType.JSON)
                .queryParam("status", "ACCEPTED")
                .when()
                .patch("/api/owner/orders/{orderId}", orderId)
                .then()
                .statusCode(200);
    }

}

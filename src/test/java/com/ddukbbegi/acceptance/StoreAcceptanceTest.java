package com.ddukbbegi.acceptance;

import com.ddukbbegi.api.auth.dto.request.LoginRequestDto;
import com.ddukbbegi.api.auth.dto.request.SignupRequestDto;
import com.ddukbbegi.api.store.repository.StoreRepository;
import com.ddukbbegi.support.config.TestContainersConfig;
import com.ddukbbegi.support.fixture.StoreFixture;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("testcontainers")
@Transactional
public class StoreAcceptanceTest extends TestContainersConfig {
    
    @LocalServerPort
    int port;

    private final String email = "test@test.com";
    private final String password = "Pwdpwdpwd1!";

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void contextLoads() {
        // 테스트 환경 정상 동작 하는지 확인하기 위해 사용
    }

    @DisplayName("1. 사장이 가게 등록 후 조회")
    @Test
    void registerStoreAndGet() {
        // given
        회원가입_사장();
        String accessToken = 로그인_및_액세스_토큰_발급();

        // when
        Long storeId = 사장_가게_등록(accessToken);

        // then
        사장가게상세_조회_검증(accessToken, storeId, "맛있는김밥");
    }

    private void 회원가입_사장() {
        given()
                .contentType(ContentType.JSON)
                .body(new SignupRequestDto(email, password, "test", "010-1234-5678", "OWNER"))
                .when()
                .post("/api/auth/signup")
                .then()
                .statusCode(200);
    }

    private String 로그인_및_액세스_토큰_발급() {
        return given()
                .contentType(ContentType.JSON)
                .body(new LoginRequestDto(email, password))
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("data.accessToken");
    }

    private Long 사장_가게_등록(String accessToken) {
        return given()
                .header("Authorization", accessToken)
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
    
    private void 사장가게상세_조회_검증(String accessToken, Long storeId, String expectedName) {
        given()
                .header("Authorization", accessToken)
                .get("/api/owner/stores/{storeId}", storeId)
                .then()
                .statusCode(200)
                .body("data.basicInfo.name", equalTo(expectedName));
    }

    private void 사장_가게_정보_수정(String accessToken, Long storeId) {

    }

}

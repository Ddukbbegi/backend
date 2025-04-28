package com.ddukbbegi.acceptance.support;

import com.ddukbbegi.api.auth.dto.request.LoginRequestDto;
import com.ddukbbegi.api.auth.dto.request.SignupRequestDto;
import com.ddukbbegi.api.menu.dto.request.NewMenuRequestDto;
import com.ddukbbegi.api.menu.repository.MenuRepository;
import com.ddukbbegi.api.store.repository.StoreRepository;
import com.ddukbbegi.api.user.enums.UserRole;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.support.fixture.MenuFixture;
import com.ddukbbegi.support.fixture.StoreFixture;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AcceptanceTestSupport {//extends TestContainersConfig {

    @Autowired protected UserRepository userRepository;
    @Autowired protected StoreRepository storeRepository;
    @Autowired protected MenuRepository menuRepository;

    @LocalServerPort
    protected int port;

    @BeforeEach
    void cleanUp() {
        menuRepository.deleteAll();
        storeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @BeforeEach
    void setUpRestAssuredPort() {
        RestAssured.port = port;
    }

    protected String 회원가입하고_로그인(String email, UserRole userRole) {
        회원가입(email, userRole);
        return 로그인_및_액세스_토큰_발급(email, userRole);
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

    protected Long 사장_가게등록(String accessToken) {
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

    protected Long 사장_메뉴등록(String accessToken, Long storeId, String menuName) {
        return given()
                .header("Authorization", accessToken)
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

}

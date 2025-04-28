package com.ddukbbegi.acceptance;

import com.ddukbbegi.acceptance.support.AcceptanceTestSupport;
import com.ddukbbegi.api.menu.dto.request.UpdatingMenuRequestDto;
import com.ddukbbegi.api.menu.dto.response.AllMenuResponseDto;
import com.ddukbbegi.api.menu.dto.response.DetailMenuResponseDto;
import com.ddukbbegi.api.menu.entity.Menu;
import com.ddukbbegi.api.menu.enums.Category;
import com.ddukbbegi.api.user.enums.UserRole;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Menu 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTestSupport {

    @DisplayName("1. (사장) 메뉴를 등록하고 수정한다.")
    @Test
    void menu() {
        // given
        String accessToken = 회원가입하고_로그인("owner21@test.com", UserRole.OWNER);
        Long storeId = 사장_가게등록(accessToken);
        Long menuId1 = 사장_메뉴등록(accessToken, storeId, "참치김밥");
        Long menuId2 = 사장_메뉴등록(accessToken, storeId, "오이김밥");
        List<AllMenuResponseDto> 메뉴목록 = 사장_메뉴목록조회(accessToken, storeId);

        // when
        사장_메뉴수정(accessToken, storeId, 메뉴목록.get(0).menuId(), "치즈김밥");

        // then
        List<Menu> storeList = menuRepository.findAllByStoreId(storeId);    // 실제 DB 검증
        assertThat(storeList.size()).isEqualTo(2);
        assertThat(storeList.get(0).getName()).isEqualTo("치즈김밥");
    }

    @DisplayName("2. (유저) 목록 목록을 조회하고 그 중 하나를 골라 상세 조회한다.")
    @Test
    void getMenusAndMenuDetail() {
        // given
        String accessToken = 회원가입하고_로그인("owner22@test.com", UserRole.OWNER);
        Long storeId = 사장_가게등록(accessToken);
        Long menuId1 = 사장_메뉴등록(accessToken, storeId, "참치김밥");
        Long menuId2 = 사장_메뉴등록(accessToken, storeId, "오이김밥");
        Long menuId3 = 사장_메뉴등록(accessToken, storeId, "치즈김밥");

        사장_메뉴삭제(accessToken, storeId, menuId2);
        List<AllMenuResponseDto> menuList = 유저_메뉴목록조회(accessToken, storeId);

        // when
        DetailMenuResponseDto 메뉴상세 = 유저_메뉴상세조회(accessToken, storeId, menuList.get(0).menuId());

        // then
        assertThat(menuList.size()).isEqualTo(2);   // 삭제된 메뉴는 조회되지 않기 때문에 데이터는 2개
        assertThat(메뉴상세.name()).isEqualTo("참치김밥");
    }

    private List<AllMenuResponseDto> 사장_메뉴목록조회(String accessToken, Long storeId) {
        return given()
                .header("Authorization", accessToken)
            .when()
                .get("/api/owner/stores/{storeId}/menus", storeId)
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data", AllMenuResponseDto.class);
    }

    private List<AllMenuResponseDto> 유저_메뉴목록조회(String accessToken, Long storeId) {
        return given()
                .header("Authorization", accessToken)
            .when()
                .get("/api/stores/{storeId}/menus", storeId)
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data", AllMenuResponseDto.class);
    }
    
    private DetailMenuResponseDto 유저_메뉴상세조회(String accessToken, Long storeId, Long menuId) {
        return given()
                .header("Authorization", accessToken)
            .when()
                .get("/api/stores/{storeId}/menus/{menuId}", storeId, menuId)
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("data", DetailMenuResponseDto.class);
    }

    private void 사장_메뉴수정(String accessToken, Long storeId, Long menuId, String menuName) {
        given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .body(new UpdatingMenuRequestDto(menuName,
                        2000,
                        "UpdatedDesc",
                        Category.MAIN_MENU))
            .when()
                .put("/api/owner/stores/{storeId}/menus/{menuId}", storeId, menuId)
            .then()
                .statusCode(200);
    }

    private void 사장_메뉴삭제(String accessToken, Long storeId, Long menuId) {
        given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .body(Map.of("status", "DELETED"))
            .when()
                .patch("/api/owner/stores/{storeId}/menus/{menuId}", storeId, menuId)
            .then()
                .statusCode(200);
    }

}

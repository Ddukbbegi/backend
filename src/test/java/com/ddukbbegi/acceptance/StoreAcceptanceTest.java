package com.ddukbbegi.acceptance;

import com.ddukbbegi.acceptance.support.AcceptanceTestSupport;
import com.ddukbbegi.api.store.dto.request.*;
import com.ddukbbegi.api.store.dto.response.StoreDetailResponseDto;
import com.ddukbbegi.api.store.dto.response.StorePageItemResponseDto;
import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.StoreStatus;
import com.ddukbbegi.api.user.enums.UserRole;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Store 인수 테스트")
public class StoreAcceptanceTest extends AcceptanceTestSupport {

    private final String email = "owner@test.com";

    @DisplayName("1. (사장) 가게 등록 후 상세 조회")
    @Test
    void registerStoreAndGet() {
        // given
        회원가입하고_로그인("owner11@test.com", UserRole.OWNER);

        // when
        Long storeId = 사장_가게등록();

        // then
        사장_가게상세_조회_검증(storeId, "맛있는김밥");
    }

    @DisplayName("2. (사장) 등록된 가게 기본 정보 및 상태 수정")
    @Test
    void updateStoreInfo() {
        // given
        회원가입하고_로그인("owner12@test.com", UserRole.OWNER);
        Long storeId = 사장_가게등록();

        // when
        가게_기본정보_수정(storeId);
        가게_임시영업중지_수정(storeId);
        
        // then
        Store store = storeRepository.findByIdOrElseThrow(storeId);
        assertThat(store.getName()).isEqualTo("더 맛있는김밥");
        assertThat(store.getStatus()).isEqualTo(StoreStatus.TEMPORARILY_CLOSED);
    }

    @DisplayName("3. (유저) 가게 목록 조회 및 가게 상세 정보 조회")
    @Test
    void getStoreListAndDetails() {
        // given
        회원가입하고_로그인("owner13@test.com", UserRole.OWNER);
        사장_가게등록();
        사장_가게등록();
        사장_가게등록();

        회원가입하고_로그인("user@test.com", UserRole.USER);
        List<StorePageItemResponseDto> responseDtoList = 유저_가게목록_조회("김밥");
        Long storeId = responseDtoList.get(0).storeId();

        // when
        StoreDetailResponseDto detailDto = 유저_가게상세_조회(storeId);

        // then
        assertThat(responseDtoList.size()).isEqualTo(3);
        assertThat(detailDto.storeId()).isEqualTo(storeId);
        assertThat(detailDto.basicInfo().name()).isEqualTo("맛있는김밥");
    }


    private void 사장_가게상세_조회_검증(Long storeId, String expectedName) {
        given()
                .header("Authorization", ownerAccessToken)
            .when()
                .get("/api/owner/stores/{storeId}", storeId)
            .then()
                .statusCode(200)
                .body("data.basicInfo.name", equalTo(expectedName));
    }

    private void 가게_기본정보_수정(Long storeId) {
        RequestStoreBasicInfo requestStoreBasicInfo = new RequestStoreBasicInfo(
                "더 맛있는김밥",
                "양식",
                "010-1234-5678",
                "신선한 재료로 매일 준비합니다."
        );
        StoreUpdateBasicInfoRequestDto dto = new StoreUpdateBasicInfoRequestDto(requestStoreBasicInfo);

        given()
                .header("Authorization", ownerAccessToken)
                .contentType(ContentType.JSON)
                .body(dto)
            .when()
                .patch("/api/owner/stores/{storeId}/basic-info", storeId)
            .then()
                .statusCode(200);
    }

    private void 가게_임시영업중지_수정(Long storeId) {
        given()
                .header("Authorization", ownerAccessToken)
                .contentType(ContentType.JSON)
                .body(new StoreUpdateStatusRequest(true))
            .when()
                .patch("/api/owner/stores/{storeId}/temporarily-close", storeId)
            .then()
                .statusCode(200);
    }

    private List<StorePageItemResponseDto> 유저_가게목록_조회(String name) {
        return given()
                .header("Authorization", userAccessToken)
                .queryParam("name", name)
            .when()
                .get("/api/stores")
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.data", StorePageItemResponseDto.class);
    }

    private StoreDetailResponseDto 유저_가게상세_조회(Long storeId) {
        return given()
                .header("Authorization", userAccessToken)
            .when()
                .get("/api/stores/{storeId}", storeId)
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("data", StoreDetailResponseDto.class);
    }

}

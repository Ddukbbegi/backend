package com.ddukbbegi.common.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@AllArgsConstructor
public enum ResultCode {

    /* JSON 결과 */
    OK(HttpStatus.OK, "A001", "요청 처리 성공"),
    CREATED(HttpStatus.CREATED, "A002", "요청 처리 성공"),
    NO_CONTENT(HttpStatus.NO_CONTENT, "A003", "요청 처리 성공"),
    FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "E001", "요청 처리 실패"),
    DB_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "E002", "요청 DB 처리 실패"),
    VALID_FAIL(HttpStatus.BAD_REQUEST, "E003", "유효성 검증에 실패하였습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "E004", "NOT FOUND"),

    /* 인증, 인가 */
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "E101", "아이디(로그인 이메일) 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해 주세요."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "E102", "Authentication failed."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "E103", "Access Denied."),

    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E999", "알 수 없는 오류"),

    /* 가게 도메인 */
    STORE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "E301", "가게 등록 가능 개수를 초과했습니다. 최대 3개까지만 등록할 수 있습니다."),
    STORE_FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "E301", "해당 가게에 접근할 권한이 없습니다."),

    /* 주문 도메인 */
    CONTAIN_DIFFERENT_STORE_MENU(HttpStatus.BAD_REQUEST,"E201","서로 다른 가게의 메뉴가 포함되어 있습니다."),
    STORE_NOT_WORKING(HttpStatus.BAD_REQUEST,"E202" ,"가게 운영중이 아닙니다." ),
    MENU_IS_DELETED(HttpStatus.BAD_REQUEST, "E203", "삭제된 메뉴가 포함되어 있습니다."),
    UNDER_MIN_DELIVERY_PRICE(HttpStatus.BAD_REQUEST, "E204", "최소 주문 금액이 충족되지 않았습니다." );

    private final HttpStatus status;
    private final String code;
    private final String defaultMessage;
}

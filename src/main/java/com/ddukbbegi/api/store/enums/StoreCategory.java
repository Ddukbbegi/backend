package com.ddukbbegi.api.store.enums;

import com.ddukbbegi.common.component.ResultCode;
import com.ddukbbegi.common.exception.BusinessException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum StoreCategory {

    KOREAN("한식"),
    CHINESE("중식"),
    JAPANESE("일식"),
    WESTERN("양식"),
    CHICKEN("치킨"),
    PIZZA("피자"),
    BURGER("버거"),
    SNACK("분식"),
    CAFE("카페/디저트"),
    LATE_NIGHT("야식"),
    BENTO("도시락/죽"),
    ASIAN("아시안"),
    MEAT("고기/구이"),
    FUSION("퓨전"),
    ETC("기타");

    private final String desc;

    StoreCategory(String desc) {
        this.desc = desc;
    }

    public static StoreCategory fromString(String name) {
        return Arrays.stream(values())
                .filter(c -> c.desc.equals(name))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ResultCode.VALID_FAIL, "존재하지 않는 카테고리명: " + name));
    }

}

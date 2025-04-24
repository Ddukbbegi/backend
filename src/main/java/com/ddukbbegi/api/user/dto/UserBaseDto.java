package com.ddukbbegi.api.user.dto;

import com.ddukbbegi.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserBaseDto {

    private Long userId; // user Id

    private String name; // 이름

    @Builder
    public static UserBaseDto fromEntity(User user) {
        return new UserBaseDto(user.getId(), user.getName());
    }
}
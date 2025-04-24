package com.ddukbbegi.api.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

public record ReviewOwnerRequestDto(
        @NotBlank
        @Size(min = 1, max = 300, message = "내용은 1자 이상 300자 이하로 입력해주세요.")
        String contents
) { }

package com.ddukbbegi.api.review.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewOwnerRequestDto {

    @NotBlank
    private String contents;
}

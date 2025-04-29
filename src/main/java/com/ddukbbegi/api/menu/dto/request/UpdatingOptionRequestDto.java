package com.ddukbbegi.api.menu.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdatingOptionRequestDto(
	@NotBlank(message = "이름은 필수 항목입니다.")
	@Size(max = 50, message = "이름은 50자 이내로 입력해주세요.")
	String name,

	@NotNull(message = "가격은 필수 항목입니다.")
	@Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
	@Max(value = 1_000_000, message = "가격은 1,000,000원 이하로 입력해주세요.")
	Integer price
) {
}

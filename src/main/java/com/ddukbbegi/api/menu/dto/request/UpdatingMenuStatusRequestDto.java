package com.ddukbbegi.api.menu.dto.request;

import com.ddukbbegi.api.menu.enums.MenuStatus;

import jakarta.validation.constraints.NotNull;

public record UpdatingMenuStatusRequestDto(
	@NotNull(message = "status는 ON_SALE, SOLD_OUT, DELETED 중 하나만 선택 가능합니다.")
	MenuStatus status
) {
}

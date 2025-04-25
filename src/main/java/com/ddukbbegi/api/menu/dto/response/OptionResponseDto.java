package com.ddukbbegi.api.menu.dto.response;

import com.ddukbbegi.api.menu.entity.Option;
import com.ddukbbegi.api.menu.enums.MenuStatus;

public record OptionResponseDto(
	String name,
	int price,
	MenuStatus status
) {
	public static OptionResponseDto toDto(Option option) {
		return new OptionResponseDto(option.getName(), option.getPrice(), option.getStatus());
	}
}

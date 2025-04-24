package com.ddukbbegi.api.menu.dto.response;

import com.ddukbbegi.api.menu.entity.Menu;

public record AllMenuResponseDto(
	String name,
	int price,
	String category
) {
	public static AllMenuResponseDto toDto(Menu menu) {
		return new AllMenuResponseDto(menu.getName(), menu.getPrice(), menu.getCategory());
	}
}
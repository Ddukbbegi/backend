package com.ddukbbegi.api.menu.dto.response;

import com.ddukbbegi.api.menu.entity.Menu;

public record DetailMenuResponseDto(
	long id,
	String name,
	int price,
	String description,
	String category
) {
	public static DetailMenuResponseDto toDto(Menu menu) {
		return new DetailMenuResponseDto(
			menu.getId(),
			menu.getName(),
			menu.getPrice(),
			menu.getDescription(),
			menu.getCategory()
		);
	}
}
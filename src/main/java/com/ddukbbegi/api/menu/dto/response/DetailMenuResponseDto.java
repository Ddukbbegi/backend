package com.ddukbbegi.api.menu.dto.response;

import com.ddukbbegi.api.menu.entity.Menu;
import com.ddukbbegi.api.menu.enums.Category;
import com.ddukbbegi.api.menu.enums.MenuStatus;

public record DetailMenuResponseDto(
	long id,
	String name,
	int price,
	String description,
	Category category,
	MenuStatus status
) {
	public static DetailMenuResponseDto toDto(Menu menu) {
		return new DetailMenuResponseDto(
			menu.getId(),
			menu.getName(),
			menu.getPrice(),
			menu.getDescription(),
			menu.getCategory(),
			menu.getStatus()
		);
	}
}
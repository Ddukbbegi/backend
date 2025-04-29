package com.ddukbbegi.api.menu.dto.response;

import com.ddukbbegi.api.menu.entity.Menu;
import com.ddukbbegi.api.menu.enums.Category;
import com.ddukbbegi.api.menu.enums.MenuStatus;

public record AllMenuResponseDto(
	Long menuId,
	String name,
	int price,
	Category category,
	MenuStatus status
) {
	public static AllMenuResponseDto toDto(Menu menu) {
		return new AllMenuResponseDto(menu.getId(), menu.getName(), menu.getPrice(), menu.getCategory(), menu.getStatus());
	}
}
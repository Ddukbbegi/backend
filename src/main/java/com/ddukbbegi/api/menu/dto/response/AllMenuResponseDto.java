package com.ddukbbegi.api.menu.dto.response;

import com.ddukbbegi.api.menu.entity.Menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AllMenuResponseDto {
	private final String name;
	private final int price;
	private final String category;

	public static AllMenuResponseDto toDto(Menu menu) {
		return new AllMenuResponseDto(menu.getName(), menu.getPrice(), menu.getCategory());
	}
}

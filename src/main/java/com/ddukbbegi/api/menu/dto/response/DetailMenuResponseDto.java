package com.ddukbbegi.api.menu.dto.response;

import com.ddukbbegi.api.menu.entity.Menu;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DetailMenuResponseDto {
	private final long id;
	private final String name;
	private final int price;
	private final String description;
	private final String category;

	@Builder
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

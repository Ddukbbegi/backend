package com.ddukbbegi.api.menu.dto.request;

import com.ddukbbegi.api.menu.entity.Menu;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @packageName    : com.ddukbbegi.api.menu.dto.request
 * @fileName       : CreateMenuDto
 * @author         : yong
 * @date           : 4/23/25
 * @description    :
 */
@Getter
@Builder
@RequiredArgsConstructor
public class NewMenuRequestDto {
	private final String name;
	private final int price;
	private final String description;
	private final String category;
	private final boolean isOption;

	public Menu fromDto(long storeId) {
		return Menu.builder()
			.name(name)
			.price(price)
			.description(description)
			.category(category)
			.isOption(isOption)
			.storeId(storeId)
			.build();
	}
}

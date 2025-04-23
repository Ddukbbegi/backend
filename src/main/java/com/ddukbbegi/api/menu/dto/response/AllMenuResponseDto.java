package com.ddukbbegi.api.menu.dto.response;

import com.ddukbbegi.api.menu.entity.Menu;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @packageName    : com.ddukbbegi.api.menu.dto.response
 * @fileName       : AllMenuResponseDto
 * @author         : yong
 * @date           : 4/23/25
 * @description    :
 */
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

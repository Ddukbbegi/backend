package com.ddukbbegi.api.menu.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UpdatingMenuRequestDto {
	private final String name;
	private final int price;
	private final String description;
	private final String category;
}

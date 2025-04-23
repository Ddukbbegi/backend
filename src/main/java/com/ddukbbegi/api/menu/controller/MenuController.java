package com.ddukbbegi.api.menu.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddukbbegi.api.menu.dto.request.NewMenuRequestDto;
import com.ddukbbegi.api.menu.service.MenuService;

import lombok.RequiredArgsConstructor;

/**
 * @packageName    : com.ddukbbegi.api.menu.controller
 * @fileName       : MenuController
 * @author         : yong
 * @date           : 4/23/25
 * @description    :
 */
@RestController
@RequestMapping("/api/stores/{storeId}")
@RequiredArgsConstructor
public class MenuController {

	private final MenuService menuService;

	@PostMapping("/menus")
	public Long addNewMenu(@PathVariable long storeId, @RequestBody NewMenuRequestDto dto) {
		return menuService.addNewMenu(storeId, dto);
	}
}

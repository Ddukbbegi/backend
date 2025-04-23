package com.ddukbbegi.api.menu.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddukbbegi.api.menu.dto.request.NewMenuRequestDto;
import com.ddukbbegi.api.menu.dto.response.DetailMenuResponseDto;
import com.ddukbbegi.api.menu.service.MenuService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stores/{storeId}/menus")
@RequiredArgsConstructor
public class MenuController {
	private final MenuService menuService;

	@GetMapping("/{menuId}")
	public DetailMenuResponseDto findDetailMenu(@PathVariable long storeId, @PathVariable long menuId) {
		return menuService.findMenuById(menuId);
	}

	@PostMapping
	public Long addNewMenu(@PathVariable long storeId, @RequestBody NewMenuRequestDto dto) {
		return menuService.addNewMenu(storeId, dto);
	}


}

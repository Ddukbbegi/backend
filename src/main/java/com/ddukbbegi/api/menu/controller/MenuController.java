package com.ddukbbegi.api.menu.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddukbbegi.api.menu.dto.request.NewMenuRequestDto;
import com.ddukbbegi.api.menu.dto.request.UpdatingMenuRequestDto;
import com.ddukbbegi.api.menu.dto.response.AllMenuResponseDto;
import com.ddukbbegi.api.menu.dto.response.DetailMenuResponseDto;
import com.ddukbbegi.api.menu.service.MenuService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stores/{storeId}/menus")
@RequiredArgsConstructor
public class MenuController {
	private final MenuService menuService;

	@GetMapping
	public List<AllMenuResponseDto> findAllMenu(@PathVariable long storeId) {
		return menuService.findAllMenuByStore(storeId);
	}

	@GetMapping("/{menuId}")
	public DetailMenuResponseDto findDetailMenu(@PathVariable long storeId, @PathVariable long menuId) {
		return menuService.findMenuById(storeId, menuId);
	}

	@PostMapping
	public Long addNewMenu(@PathVariable long storeId, @RequestBody NewMenuRequestDto dto) {
		return menuService.addNewMenu(storeId, dto);
	}

	@PutMapping("/{menuId}")
	public void updateMenu(@PathVariable long storeId, @PathVariable long menuId,
		@RequestBody UpdatingMenuRequestDto dto) {
		menuService.updateMenuById(menuId, dto);
	}

	@DeleteMapping("/{menuId}")
	public void deleteMenu(@PathVariable long storeId, @PathVariable long menuId) {
		menuService.deleteMenuById(menuId);
	}

}

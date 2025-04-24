package com.ddukbbegi.api.menu.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddukbbegi.api.menu.dto.request.NewMenuRequestDto;
import com.ddukbbegi.api.menu.dto.request.UpdatingMenuRequestDto;
import com.ddukbbegi.api.menu.dto.request.UpdatingMenuStatusRequestDto;
import com.ddukbbegi.api.menu.dto.response.AllMenuResponseDto;
import com.ddukbbegi.api.menu.service.MenuService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/owner/stores/{storeId}/menus")
@RequiredArgsConstructor
public class OwnerMenuController {
	private final MenuService menuService;

	// 사장이 가게의 모든 상태의 메뉴를 확인 할 때(상태: 품절된 메뉴, 판매중인 메뉴, 삭제된 메뉴)
	@GetMapping
	public List<AllMenuResponseDto> findAllMenusForOwner(@PathVariable long storeId) {
		return menuService.findAllMenusForOwner(storeId);
	}

	// 메뉴 추가
	@PostMapping
	public Long addNewMenu(@PathVariable long storeId, @Valid @RequestBody NewMenuRequestDto dto) {
		return menuService.addNewMenu(storeId, dto);
	}

	// 메뉴 수정
	@PutMapping("/{menuId}")
	public void updateMenu(@PathVariable long storeId, @PathVariable long menuId,
		@Valid @RequestBody UpdatingMenuRequestDto dto) {
		menuService.updateMenu(menuId, dto);
	}

	// 메뉴 상태 변경
	@PatchMapping("/{menuId}")
	public void switchMenuStatus(@PathVariable long storeId, @PathVariable long menuId, @RequestBody
	UpdatingMenuStatusRequestDto dto) {
		menuService.switchMenuStatus(menuId, dto);
	}
}

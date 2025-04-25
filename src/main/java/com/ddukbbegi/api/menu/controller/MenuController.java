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
import com.ddukbbegi.api.menu.dto.request.UpdatingMenuStatusRequestDto;
import com.ddukbbegi.api.menu.dto.response.AllMenuResponseDto;
import com.ddukbbegi.api.menu.dto.response.DetailMenuResponseDto;
import com.ddukbbegi.api.menu.service.MenuService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stores/{storeId}/menus")
@RequiredArgsConstructor
public class MenuController {
	private final MenuService menuService;

	// 모든 유저가 한 가게의 품절된 메뉴와 판매중인 메뉴를 확인 할 때
	@GetMapping()
	public List<AllMenuResponseDto> findVisibleMenus(@PathVariable long storeId) {
		return menuService.findAllMenusForCustomer(storeId);
	}

	// 모든 유저가 한 메뉴의 정보를 확인할 때 (상태: 품절된 메뉴, 판매중인 메뉴)
	@GetMapping("/{menuId}")
	public DetailMenuResponseDto findDetailMenu(@PathVariable long storeId, @PathVariable long menuId) {
		return menuService.findMenu(storeId, menuId);
	}
}

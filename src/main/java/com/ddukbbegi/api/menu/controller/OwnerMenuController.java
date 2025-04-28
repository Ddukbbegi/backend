package com.ddukbbegi.api.menu.controller;

import java.util.List;

import com.ddukbbegi.api.user.entity.CustomUserDetails;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
	public BaseResponse<List<AllMenuResponseDto>> findAllMenusForOwner(@PathVariable long storeId,
																	   @AuthenticationPrincipal CustomUserDetails userDetails) {
		return BaseResponse.success(menuService.findAllMenusForOwner(storeId, userDetails.getUserId()), ResultCode.OK);
	}

	// 메뉴 추가
	@PostMapping
	public BaseResponse<Long> addNewMenu(@PathVariable long storeId, @Valid @RequestBody NewMenuRequestDto dto,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		return BaseResponse.success(menuService.addNewMenu(userDetails.getUserId(),storeId, dto), ResultCode.CREATED);
	}

	// 메뉴 수정
	@PutMapping("/{menuId}")
	public BaseResponse<Void> updateMenu(@PathVariable long storeId, @PathVariable long menuId,
		@Valid @RequestBody UpdatingMenuRequestDto dto,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		menuService.updateMenu(userDetails.getUserId(), menuId, dto);
		return BaseResponse.success(ResultCode.NO_CONTENT);
	}

	// 메뉴 상태 변경
	@PatchMapping("/{menuId}")
	public BaseResponse<Void> switchMenuStatus(@PathVariable long storeId, @PathVariable long menuId, @RequestBody
	UpdatingMenuStatusRequestDto dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
		menuService.switchMenuStatus(userDetails.getUserId(), menuId, dto);
		return BaseResponse.success(ResultCode.NO_CONTENT);
	}
}

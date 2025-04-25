package com.ddukbbegi.api.menu.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddukbbegi.api.menu.dto.request.NewOptionRequestDto;
import com.ddukbbegi.api.menu.dto.request.SwitchingOptionStatusRequestDto;
import com.ddukbbegi.api.menu.dto.request.UpdatingOptionRequestDto;
import com.ddukbbegi.api.menu.service.OptionServiceImpl;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/owner/menus/{menuId}/options")
@RequiredArgsConstructor
public class OwnerOptionController {
	private final OptionServiceImpl optionService;

	// 옵션 메뉴 생성
	@PostMapping
	public BaseResponse<Long> addNewOption(@PathVariable long menuId, @RequestBody NewOptionRequestDto dto) {
		return BaseResponse.success(optionService.addNewOptionMenu(menuId, dto), ResultCode.CREATED);
	}

	// 옵션 메뉴 수정
	@PutMapping("/{optionId}")
	public BaseResponse<Void> updateOption(@PathVariable long menuId, @PathVariable long optionId, @Valid @RequestBody UpdatingOptionRequestDto dto) {
		optionService.updateOption(menuId, optionId, dto);
		return BaseResponse.success(ResultCode.NO_CONTENT);
	}
	// 옵션 메뉴 상태 변경
	@PatchMapping("/{optionId}")
	public BaseResponse<Void> updateOptionStatus(@PathVariable long optionId, @Valid @RequestBody SwitchingOptionStatusRequestDto dto) {
		optionService.switchOptionStatus(optionId, dto);
		return BaseResponse.success(ResultCode.NO_CONTENT);
	}
}

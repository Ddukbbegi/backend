package com.ddukbbegi.api.menu.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddukbbegi.api.menu.dto.request.NewOptionRequestDto;
import com.ddukbbegi.api.menu.service.OptionServiceImpl;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/owner/options")
@RequiredArgsConstructor
public class OwnerOptionController {
	private final OptionServiceImpl optionService;

	// 옵션 메뉴 생성
	@PostMapping
	public void addNewOptionMenu(@RequestBody NewOptionRequestDto dto) {
		BaseResponse.success(optionService.addNewOptionMenu(dto), ResultCode.CREATED);
	}

	// 옵션 메뉴 수정
	// 옵션 메뉴 상태 변경
}

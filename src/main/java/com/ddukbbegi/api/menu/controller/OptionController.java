package com.ddukbbegi.api.menu.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddukbbegi.api.menu.dto.response.OptionResponseDto;
import com.ddukbbegi.api.menu.service.OptionServiceImpl;
import com.ddukbbegi.common.component.BaseResponse;
import com.ddukbbegi.common.component.ResultCode;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/options")
@RequiredArgsConstructor
public class OptionController {
	private final OptionServiceImpl optionService;

	// 옵션 메뉴, 메뉴로 조회
	@GetMapping("/menus/{menuId}")
	public BaseResponse<List<OptionResponseDto>> findAllByMenu(@PathVariable long menuId) {
		return BaseResponse.success(optionService.findAllByMenu(menuId), ResultCode.OK);
	}
}

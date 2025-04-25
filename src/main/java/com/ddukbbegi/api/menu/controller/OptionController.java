package com.ddukbbegi.api.menu.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddukbbegi.api.menu.service.OptionServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/options")
@RequiredArgsConstructor
public class OptionController {
	private final OptionServiceImpl optionService;
	// 옵션 메뉴, 메뉴로 조회

}

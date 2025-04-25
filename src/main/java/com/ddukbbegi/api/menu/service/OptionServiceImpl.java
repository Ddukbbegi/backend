package com.ddukbbegi.api.menu.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddukbbegi.api.menu.dto.request.NewOptionRequestDto;
import com.ddukbbegi.api.menu.entity.Menu;
import com.ddukbbegi.api.menu.entity.Option;
import com.ddukbbegi.api.menu.repository.OptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OptionServiceImpl {
	private final OptionRepository optionRepository;

	@Transactional
	public long addNewOptionMenu(NewOptionRequestDto dto) {
		Option option = optionRepository.save(dto.toEntity());
		return option.getId();
	}
}

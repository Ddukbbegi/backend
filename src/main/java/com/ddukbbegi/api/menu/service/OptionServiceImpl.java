package com.ddukbbegi.api.menu.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddukbbegi.api.menu.dto.request.NewOptionRequestDto;
import com.ddukbbegi.api.menu.dto.request.SwitchingOptionStatusRequestDto;
import com.ddukbbegi.api.menu.dto.request.UpdatingOptionRequestDto;
import com.ddukbbegi.api.menu.dto.response.AllMenuResponseDto;
import com.ddukbbegi.api.menu.dto.response.DetailMenuResponseDto;
import com.ddukbbegi.api.menu.dto.response.OptionResponseDto;
import com.ddukbbegi.api.menu.entity.Menu;
import com.ddukbbegi.api.menu.entity.Option;
import com.ddukbbegi.api.menu.enums.MenuStatus;
import com.ddukbbegi.api.menu.repository.MenuRepository;
import com.ddukbbegi.api.menu.repository.OptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OptionServiceImpl {
	private final OptionRepository optionRepository;
	private final MenuRepository menuRepository;

	@Transactional
	public long addNewOptionMenu(long menuId, NewOptionRequestDto dto) {
		Menu menu = menuRepository.findByIdOrElseThrow(menuId);
		Option option = optionRepository.save(dto.toEntity(menu));
		return option.getId();
	}

	@Transactional
	public void updateOption(long menuId, long optionId, UpdatingOptionRequestDto dto) {
		Option option = optionRepository.findByIdOrElseThrow(optionId);
		option.update(dto.name(), dto.price());
	}

	@Transactional(readOnly = true)
	public List<OptionResponseDto> findAllByMenu(long menuId) {
		Menu menu = menuRepository.findByIdOrElseThrow(menuId);
		return optionRepository.findAllByMenu(menu).stream()
			.map(option -> OptionResponseDto.toDto(option))
			.collect(Collectors.toList());
	}

	@Transactional
	public void switchOptionStatus(long optionId, SwitchingOptionStatusRequestDto dto) {
		optionRepository.switchOptionStatusById(optionId, dto.status());
	}
}

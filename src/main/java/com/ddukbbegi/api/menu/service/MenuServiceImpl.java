package com.ddukbbegi.api.menu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ddukbbegi.api.menu.dto.request.NewMenuRequestDto;
import com.ddukbbegi.api.menu.dto.request.UpdatingMenuRequestDto;
import com.ddukbbegi.api.menu.dto.response.AllMenuResponseDto;
import com.ddukbbegi.api.menu.dto.response.DetailMenuResponseDto;
import com.ddukbbegi.api.menu.entity.Menu;
import com.ddukbbegi.api.menu.repository.MenuRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService{
	private final MenuRepository menuRepository;

	@Override
	public List<AllMenuResponseDto> findAllMenuByStore(long storeId){
		return menuRepository.findAllMenuByStore(storeId).stream()
			.map(menu -> AllMenuResponseDto.toDto(menu))
			.collect(Collectors.toList());
	}

	@Override
	public DetailMenuResponseDto findMenuById(long storeId, long id) {
		Menu menu = menuRepository.findMenuById(storeId, id);
		return DetailMenuResponseDto.toDto(menu);
	}

	@Override
	@Transactional
	public Long addNewMenu(long storeId, NewMenuRequestDto dto) {
		Menu menu = menuRepository.save(dto.fromDto(storeId));
		return menu.getId();
	}

	@Override
	@Transactional
	public void updateMenuById(long id, UpdatingMenuRequestDto dto) {
		Menu menu = menuRepository.findById(id).orElseThrow();
		menu.update(dto.getName(), dto.getPrice(), dto.getDescription(), dto.getCategory());
		DetailMenuResponseDto.toDto(menu);
	}

	@Override
	@Transactional
	public void deleteMenuById(long id) {
		Menu menu = menuRepository.findById(id).orElseThrow();
		menu.delete();
	}
}

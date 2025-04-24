package com.ddukbbegi.api.menu.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ddukbbegi.api.menu.dto.request.NewMenuRequestDto;
import com.ddukbbegi.api.menu.dto.request.UpdatingMenuRequestDto;
import com.ddukbbegi.api.menu.dto.request.UpdatingMenuStatusRequestDto;
import com.ddukbbegi.api.menu.dto.response.AllMenuResponseDto;
import com.ddukbbegi.api.menu.dto.response.DetailMenuResponseDto;
import com.ddukbbegi.api.menu.entity.Menu;
import com.ddukbbegi.api.menu.enums.MenuStatus;
import com.ddukbbegi.api.menu.repository.MenuRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService{
	private final MenuRepository menuRepository;

	@Override
	@Transactional(readOnly = true)
	public List<AllMenuResponseDto> findAllMenusForOwner(long storeId){
		return menuRepository.findAllByStoreId(storeId).stream()
			.map(menu -> AllMenuResponseDto.toDto(menu))
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<AllMenuResponseDto> findAllMenusForCustomer(long storeId){
		return menuRepository.findAllByStoreIdAndStatusNot(storeId, MenuStatus.DELETED).stream()
			.map(menu -> AllMenuResponseDto.toDto(menu))
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public DetailMenuResponseDto findMenu(long storeId, long id) {
		Menu menu = menuRepository.findMenuByIdAndStoreId(storeId, id);
		return DetailMenuResponseDto.toDto(menu);
	}

	@Override
	@Transactional
	public Long addNewMenu(long storeId, NewMenuRequestDto dto) {
		Menu menu = menuRepository.save(dto.toEntity(storeId));
		return menu.getId();
	}

	@Override
	@Transactional
	public void updateMenu(long id, UpdatingMenuRequestDto dto) {
		Menu menu = menuRepository.findById(id).orElseThrow();
		menu.update(dto.name(), dto.price(), dto.description(), dto.category());
		DetailMenuResponseDto.toDto(menu);
	}

	@Override
	@Transactional
	public void switchMenuStatus(long id, UpdatingMenuStatusRequestDto dto) {
		menuRepository.updateMenuStatusById(id, dto.status());
	}
}

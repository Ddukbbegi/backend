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
import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.repository.StoreRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService{
	private final MenuRepository menuRepository;
	private final StoreRepository storeRepository;

	@Override
	@Transactional(readOnly = true)
	public List<AllMenuResponseDto> findAllMenusForOwner(long storeId, long userId){
		if(!isStoreOwner(userId, storeId)) {
			throw new RuntimeException("이 가게 사장이 아니군요!");
		};

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
	public Long addNewMenu(long userId, long storeId, NewMenuRequestDto dto) {
		if(!isStoreOwner(userId, storeId)) {
			throw new RuntimeException("이 가게 사장이 아니군요!");
		};

		Store store = storeRepository.findByIdOrElseThrow(storeId);
		Menu menu = menuRepository.save(dto.toEntity(store));
		return menu.getId();
	}

	@Override
	@Transactional
	public void updateMenu(long userId, long id, UpdatingMenuRequestDto dto) {
		if(!isMenuOwner(userId, id)) {
			throw new RuntimeException("이 가게 사장이 아니군요!");
		};

		Menu menu = menuRepository.findById(id).orElseThrow();
		menu.update(dto.name(), dto.price(), dto.description(), dto.category());
		DetailMenuResponseDto.toDto(menu);
	}

	@Override
	@Transactional
	public void switchMenuStatus(long userId, long id, UpdatingMenuStatusRequestDto dto) {
		if(!isMenuOwner(userId, id)) {
			throw new RuntimeException("이 가게 사장이 아니군요!");
		};
		menuRepository.updateMenuStatusById(id, dto.status());
	}

	private boolean isStoreOwner(long userId, long storeId) {
		return menuRepository.isStoreOwner(storeId, userId);
	}

	private boolean isMenuOwner(long userId, long menuId) {
		return menuRepository.isMenuOwner(menuId, userId);
	}
}

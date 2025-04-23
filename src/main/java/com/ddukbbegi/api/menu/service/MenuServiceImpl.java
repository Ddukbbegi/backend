package com.ddukbbegi.api.menu.service;

import org.springframework.stereotype.Service;

import com.ddukbbegi.api.menu.dto.request.NewMenuRequestDto;
import com.ddukbbegi.api.menu.entity.Menu;
import com.ddukbbegi.api.menu.repository.MenuRepository;

import lombok.RequiredArgsConstructor;

/**
 * @packageName    : com.ddukbbegi.api.menu.service
 * @fileName       : MenuServiceImpl
 * @author         : yong
 * @date           : 4/23/25
 * @description    :
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService{
	private final MenuRepository menuRepository;

	@Override
	public Long addNewMenu(long storeId, NewMenuRequestDto dto) {
		Menu menu = menuRepository.save(dto.fromDto(storeId));
		return menu.getId();
	}
}

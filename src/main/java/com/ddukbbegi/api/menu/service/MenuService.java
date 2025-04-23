package com.ddukbbegi.api.menu.service;

import java.util.List;

import com.ddukbbegi.api.menu.dto.request.NewMenuRequestDto;
import com.ddukbbegi.api.menu.dto.request.UpdatingMenuRequestDto;
import com.ddukbbegi.api.menu.dto.response.AllMenuResponseDto;
import com.ddukbbegi.api.menu.dto.response.DetailMenuResponseDto;

public interface MenuService {
	List<AllMenuResponseDto> findAllMenuByStore(long storeId);

	DetailMenuResponseDto findMenuById(long storeId, long menuId);

	Long addNewMenu(long storeId, NewMenuRequestDto dto);

	void updateMenuById(long id, UpdatingMenuRequestDto dto);

	void deleteMenuById(long menuId);
}

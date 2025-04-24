package com.ddukbbegi.api.menu.service;

import java.util.List;

import com.ddukbbegi.api.menu.dto.request.NewMenuRequestDto;
import com.ddukbbegi.api.menu.dto.request.UpdatingMenuRequestDto;
import com.ddukbbegi.api.menu.dto.response.AllMenuResponseDto;
import com.ddukbbegi.api.menu.dto.response.DetailMenuResponseDto;

public interface MenuService {
	List<AllMenuResponseDto> findAllMenu(long storeId);

	DetailMenuResponseDto findMenu(long storeId, long menuId);

	Long addNewMenu(long storeId, NewMenuRequestDto dto);

	void updateMenu(long id, UpdatingMenuRequestDto dto);

	void deleteMenu(long menuId);
}

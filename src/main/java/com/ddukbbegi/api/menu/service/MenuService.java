package com.ddukbbegi.api.menu.service;

import java.util.List;

import com.ddukbbegi.api.menu.dto.request.NewMenuRequestDto;
import com.ddukbbegi.api.menu.dto.request.UpdatingMenuRequestDto;
import com.ddukbbegi.api.menu.dto.request.UpdatingMenuStatusRequestDto;
import com.ddukbbegi.api.menu.dto.response.AllMenuResponseDto;
import com.ddukbbegi.api.menu.dto.response.DetailMenuResponseDto;
import com.ddukbbegi.api.menu.enums.MenuStatus;

public interface MenuService {
	List<AllMenuResponseDto> findAllMenusForOwner(long storeId);

	List<AllMenuResponseDto> findAllMenusForCustomer(long storeId);

	DetailMenuResponseDto findMenu(long storeId, long menuId);

	Long addNewMenu(long storeId, NewMenuRequestDto dto);

	void updateMenu(long id, UpdatingMenuRequestDto dto);

	void switchMenuStatus(long menuId, UpdatingMenuStatusRequestDto dto);
}

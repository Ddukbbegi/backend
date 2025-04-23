package com.ddukbbegi.api.menu.service;

import com.ddukbbegi.api.menu.dto.request.NewMenuRequestDto;
import com.ddukbbegi.api.menu.dto.request.UpdatingMenuRequestDto;
import com.ddukbbegi.api.menu.dto.response.DetailMenuResponseDto;

public interface MenuService {
	DetailMenuResponseDto findMenuById(long menuId);

	Long addNewMenu(long storeId, NewMenuRequestDto dto);

	void updateMenuById(long id, UpdatingMenuRequestDto dto);
}

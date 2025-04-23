package com.ddukbbegi.api.menu.service;

import com.ddukbbegi.api.menu.dto.request.NewMenuRequestDto;
import com.ddukbbegi.api.menu.dto.response.DetailMenuResponseDto;

/**
 * @packageName    : com.ddukbbegi.api.menu.service
 * @fileName       : MenuService
 * @author         : yong
 * @date           : 4/23/25
 * @description    :
 */
public interface MenuService {
	DetailMenuResponseDto findMenuById(long menuId);

	Long addNewMenu(long storeId, NewMenuRequestDto dto);
}

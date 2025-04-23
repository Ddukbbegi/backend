package com.ddukbbegi.api.menu.service;

import com.ddukbbegi.api.menu.dto.request.NewMenuRequestDto;

/**
 * @packageName    : com.ddukbbegi.api.menu.service
 * @fileName       : MenuService
 * @author         : yong
 * @date           : 4/23/25
 * @description    :
 */
public interface MenuService {
	Long addNewMenu(long storeId, NewMenuRequestDto dto);
}

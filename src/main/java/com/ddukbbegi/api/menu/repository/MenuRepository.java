package com.ddukbbegi.api.menu.repository;

import org.springframework.data.jpa.repository.Query;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.menu.entity.Menu;

public interface MenuRepository extends BaseRepository<Menu, Long> {
	@Query("SELECT m FROM Menu m WHERE m.storeId = :storeId AND m.id = :id AND m.isDeleted = false")
	Menu findMenuById(long storeId, long id);
}

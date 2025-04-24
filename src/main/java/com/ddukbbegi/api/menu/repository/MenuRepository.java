package com.ddukbbegi.api.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.menu.entity.Menu;

public interface MenuRepository extends BaseRepository<Menu, Long> {
	@Query("SELECT m FROM Menu m WHERE m.id = :id AND m.storeId = :storeId AND m.isDeleted = false")
	Menu findMenuByIdAndStoreIdAndIsDeletedFalse(long storeId, long id);

	@Query("SELECT m FROM Menu m WHERE m.storeId = :storeId AND m.isDeleted = false")
	List<Menu> findAllMenuByStoreAndIsDeletedFalse(long storeId);
}

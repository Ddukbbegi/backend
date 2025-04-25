package com.ddukbbegi.api.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.menu.entity.Menu;
import com.ddukbbegi.api.menu.enums.MenuStatus;

public interface MenuRepository extends BaseRepository<Menu, Long> {
	@Query("SELECT m FROM Menu m WHERE m.id = :id AND m.storeId = :storeId")
	Menu findMenuByIdAndStoreId(@Param("storeId") long storeId, @Param("id") long id);

	@Query("SELECT m FROM Menu m WHERE m.storeId = :storeId AND m.status != :status")
	List<Menu> findAllByStoreIdAndStatusNot(@Param("storeId") long storeId, @Param("status") MenuStatus status);

	@Query("SELECT m FROM Menu m WHERE m.storeId = :storeId")
	List<Menu> findAllByStoreId(@Param("storeId") long storeId);

	@Modifying
	@Query("UPDATE Menu m SET m.status = :status WHERE m.id = :id")
	void updateMenuStatusById(@Param("id") long id, @Param("status") MenuStatus status);

	List<Menu> findAllByIdInAndIsDeletedFalse(List<Long> menuIds);
}

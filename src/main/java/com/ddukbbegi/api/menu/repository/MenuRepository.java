package com.ddukbbegi.api.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.menu.entity.Menu;
import com.ddukbbegi.api.menu.enums.MenuStatus;

public interface MenuRepository extends BaseRepository<Menu, Long> {
	@Query("SELECT m FROM Menu m WHERE m.id = :id AND m.store.id = :storeId")
	Menu findMenuByIdAndStoreId(@Param("storeId") long storeId, @Param("id") long id);

	@Query("SELECT m FROM Menu m WHERE m.store.id = :storeId AND m.status != :status")
	List<Menu> findAllByStoreIdAndStatusNot(@Param("storeId") long storeId, @Param("status") MenuStatus status);

	@Query("SELECT m FROM Menu m WHERE m.store.id = :storeId")
	List<Menu> findAllByStoreId(@Param("storeId") long storeId);

	@Modifying
	@Query("UPDATE Menu m SET m.status = :status WHERE m.id = :id")
	void updateMenuStatusById(@Param("id") long id, @Param("status") MenuStatus status);

	@Query("SELECT m FROM Menu m WHERE m.id IN :menuIds AND m.status != 'DELETED'")
	List<Menu> findAllByIdInAndNotDeleted(@Param("menuIds") List<Long> menuIds);

	// @Query("SELECT COUNT(s) > 0 FROM Store s WHERE s.id = :storeId AND s.user.id = :userId")
	// boolean checkOwnerTest(@Param("storeId") Long storeId, @Param("userId") Long userId);

	@Query("SELECT COUNT(s) > 0 FROM Store s WHERE s.user.id = :userId")
	boolean isStoreOwner(@Param("menuId") long menuId, @Param("userId") long userId);

	@Query("SELECT COUNT(m) > 0 FROM Menu m JOIN m.store s WHERE m.id = :menuId AND s.user.id = :userId")
	boolean isMenuOwner(@Param("menuId") long menuId, @Param("userId") long userId);
}

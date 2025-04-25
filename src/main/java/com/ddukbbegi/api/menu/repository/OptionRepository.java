package com.ddukbbegi.api.menu.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.menu.entity.Option;
import com.ddukbbegi.api.menu.enums.MenuStatus;

public interface OptionRepository extends BaseRepository<Option, Long> {
	@Modifying
	@Query("UPDATE Menu m SET m.status = :status WHERE m.id = :id")
	void updateOptionStatusById(@Param("id") long id, @Param("status") MenuStatus status);

}

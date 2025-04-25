package com.ddukbbegi.api.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.menu.entity.Menu;
import com.ddukbbegi.api.menu.entity.Option;
import com.ddukbbegi.api.menu.enums.MenuStatus;

public interface OptionRepository extends BaseRepository<Option, Long> {
	List<Option> findAllByMenu(Menu menu);

	@Modifying
	@Query("UPDATE Option o SET o.status = :status WHERE o.id = :id")
	void switchOptionStatusById(long id, MenuStatus status);
}

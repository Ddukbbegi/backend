package com.ddukbbegi.api.menu.repository;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.menu.entity.Menu;

import java.util.List;

public interface MenuRepository extends BaseRepository<Menu, Long> {
    List<Menu> findAllByIdInAndIsDeletedFalse(List<Long> menuIds);
}

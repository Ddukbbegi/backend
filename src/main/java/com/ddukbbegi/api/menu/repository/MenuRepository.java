package com.ddukbbegi.api.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddukbbegi.api.menu.entity.Menu;

/**
 * @packageName    : com.ddukbbegi.api.menu.repository
 * @fileName       : MenuRepository
 * @author         : yong
 * @date           : 4/23/25
 * @description    :
 */
public interface MenuRepository extends JpaRepository<Menu, Long> {
}

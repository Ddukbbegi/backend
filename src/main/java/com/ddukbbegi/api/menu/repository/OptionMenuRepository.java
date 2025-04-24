package com.ddukbbegi.api.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddukbbegi.api.menu.entity.Option;

public interface OptionMenuRepository extends JpaRepository<Option, Long> {
}

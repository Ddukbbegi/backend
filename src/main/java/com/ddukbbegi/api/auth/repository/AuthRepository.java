package com.ddukbbegi.api.auth.repository;

import com.ddukbbegi.api.auth.entity.Auth;
import org.springframework.data.repository.CrudRepository;

public interface AuthRepository extends CrudRepository<Auth, String> {

    void deleteByUserId(Long userId);
}

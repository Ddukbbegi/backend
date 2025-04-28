package com.ddukbbegi.api.user.repository;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.user.entity.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long> {

    boolean existsByIdAndIsDeletedTrue(Long id);

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndIsDeletedFalse(String email);

    Optional<User> findByEmail(String email);
}

package com.ddukbbegi.common.auth;

import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.common.component.ResultCode;
import com.ddukbbegi.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // 데이터베이스에서 userId로 사용자 조회
        User userEntity = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() ->
                        new BusinessException(ResultCode.NOT_FOUND));

        // 사용자 정보를 CustomUserDetails 객체로 반환
        return new CustomUserDetails(
                userEntity.getId(),
                getAuthorities(userEntity)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User userEntity) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userEntity.getUserRole().toString()));
    }
}
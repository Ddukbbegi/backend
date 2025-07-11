package com.ddukbbegi.api.user.entity;


import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {
    private Long userId;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long userId, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.authorities = authorities;
    }

    public Long getUserId() {
        return userId;
    }

    // Username 대신 userId 로 설정
    @Override
    public String getUsername() {
        return String.valueOf(userId);  // 이메일 (아이디)
    }

    // JWT 인증 방식에서는 Password 불필요하여 해당 부분 공백으로 반환
    @Override
    public String getPassword() {
        return " ";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
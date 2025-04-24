package com.ddukbbegi.common.auth;


import com.ddukbbegi.api.user.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {
    private Long userId;
    private String email;
    private String password;
    private UserRole userRole;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long userId, String email, String password, UserRole userRole, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.authorities = authorities;
    }

    public Long getUserId() {
        return userId;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    @Override
    public String getUsername() {
        return email;  // 이메일 (아이디)
    }

    @Override
    public String getPassword() {
        return password;
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
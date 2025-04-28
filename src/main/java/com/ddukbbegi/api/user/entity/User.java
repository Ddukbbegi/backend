package com.ddukbbegi.api.user.entity;

import com.ddukbbegi.api.common.entity.BaseTimeEntity;
import com.ddukbbegi.api.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private String name;

    private String phone;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(nullable = false)
    private boolean isDeleted = Boolean.FALSE;

    // --- 소셜 로그인 관련 추가 필드 ---
    private String provider;  // "google", "kakao", "naver" (없으면 일반 로그인 유저)
    private String providerId; // 소셜에서 내려주는 고유 ID

    @Builder
    public User(String email, String password, String name, String phone, UserRole userRole, boolean isDeleted, String provider, String providerId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.userRole = userRole;
        this.isDeleted = isDeleted;
        this.provider = provider;
        this.providerId = providerId;
    }

    public User(String email, String password, String name, String phone, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.userRole = userRole;
    }

    public static User of(String email, String password, String name, String phone, UserRole userRole) {
        return new User(email, password, name, phone, userRole);
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updatePhone(String phone) {
        this.phone = phone;
    }

    // getAuthorities() 메서드
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    public Long getId() {
        return id;
    }
}

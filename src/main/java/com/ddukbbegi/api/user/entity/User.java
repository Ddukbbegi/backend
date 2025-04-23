package com.ddukbbegi.api.user.entity;

import com.ddukbbegi.api.common.entity.BaseTimeEntity;
import com.ddukbbegi.api.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String phone;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private boolean isDeleted = Boolean.FALSE;

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

}

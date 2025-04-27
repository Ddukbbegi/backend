package com.ddukbbegi.api.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.ddukbbegi.api.user.entity.User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User extends org.springframework.security.core.userdetails.User implements OAuth2User {

    private final User user;
    private final Map<String, Object> attributes;
    private final String provider;
    private final String providerId;

    public CustomOAuth2User(
            com.ddukbbegi.api.user.entity.User user,
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            Map<String, Object> attributes,
            String provider,
            String providerId) {
        super(username, password, authorities);
        this.user = user;
        this.attributes = attributes;
        this.provider = provider;
        this.providerId = providerId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

    public Long getId() {
        return user.getId();  // User 클래스에서 자동으로 생성된 ID 값
    }

    public String getProvider() {
        return provider;
    }

    public String getProviderId() {
        return providerId;
    }
}

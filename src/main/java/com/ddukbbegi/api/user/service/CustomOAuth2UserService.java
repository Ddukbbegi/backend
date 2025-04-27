package com.ddukbbegi.api.user.service;

import com.ddukbbegi.api.user.entity.CustomOAuth2User;
import com.ddukbbegi.api.user.entity.GoogleUserInfo;
import com.ddukbbegi.api.user.entity.OAuth2UserInfo;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.enums.UserRole;
import com.ddukbbegi.api.user.repository.UserRepository;
import com.ddukbbegi.common.config.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo userInfo = getUserInfo(oAuth2User, provider);

        String email = userInfo.getEmail();
        com.ddukbbegi.api.user.entity.User user = userRepository.findByEmail(email)
                .orElseGet(() -> createUser(userInfo));

        if (user.getProvider() == null) {
            throw new OAuth2AuthenticationException("이미 일반 회원가입된 이메일입니다. 소셜 로그인 불가합니다.");
        }

        if (!user.getProvider().equals(provider)) {
            throw new OAuth2AuthenticationException("다른 소셜 계정으로 가입된 이메일입니다.");
        }

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(user,
                user.getName(),
                user.getPassword(),
                user.getAuthorities(),
                oAuth2User.getAttributes(),
                userInfo.getProvider(),
                userInfo.getProviderId());

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities()));

        return customOAuth2User;
    }

    /**
     * OAuth2 가 어떤 Provider 에 따라 UserInfo 반환
     *
     * @param oAuth2User
     * @param provider
     * @return OAuth2UserInfo
     */
    private OAuth2UserInfo getUserInfo(OAuth2User oAuth2User, String provider) {
        switch (provider) {
            case "google":
                return new GoogleUserInfo(oAuth2User.getAttributes());
            default:
                throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다.");
        }
    }

    /**
     * User Repository 에 저장
     *
     * @param userInfo
     * @return User
     */
    private User createUser(OAuth2UserInfo userInfo) {
        User user = User.builder()
                .email(userInfo.getEmail())
                .password(passwordEncoder.encode("social"))
                .name(userInfo.getName())
                .phone(null)
                .userRole(UserRole.USER)
                .isDeleted(false)
                .provider(userInfo.getProvider())
                .providerId(userInfo.getProviderId())
                .build();
        return userRepository.save(user);
    }
}

package com.ddukbbegi.common.config;

import com.ddukbbegi.common.auth.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails) {
            Long userId = ((CustomUserDetails) principal).getUserId();
            return Optional.of(userId);
        }

        return Optional.empty();
    }

}

package com.ddukbbegi.common.config.aop;

import com.ddukbbegi.api.user.entity.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OwnerAccessLoggingAspect {
    private final HttpServletRequest request;
    private final ObjectMapper objectMapper;

    @Around("@annotation(com.ddukbbegi.common.config.aop.Owner)")
    public Object ownerApiAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. SecurityContextHolder 에서 userId Get
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        // 2. url & 현재 시간
        String url = request.getRequestURI();
        long requestTimestamp = System.currentTimeMillis();

        // 3. 요청 데이터(RequestBody)
        String requestBody = objectMapper.writeValueAsString(joinPoint.getArgs());
        log.info("AOP - Owner API Request: userId={}, Timestamp={}, URL={}, RequestBody={}, ip={}",
                userId, requestTimestamp, url, requestBody, request.getRemoteAddr());

        // 4. 메서드 실행
        Object result = joinPoint.proceed();

        // 5. API 응답 데이터를 JSON 으로 변환
        String responseBody = objectMapper.writeValueAsString(result);
        log.info("AOP - Owner API Response: userId={}, Timestamp={}, URL={}, ResponseBody={}, ip={}",
                userId, System.currentTimeMillis(), url, responseBody, request.getRemoteAddr());

        return result;
    }
}

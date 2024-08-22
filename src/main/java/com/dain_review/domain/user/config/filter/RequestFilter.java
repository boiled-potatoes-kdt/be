package com.dain_review.domain.user.config.filter;

import com.dain_review.domain.user.config.model.CustomUserDetails;
import com.dain_review.domain.user.exception.errortype.AuthErrorCode;
import com.dain_review.domain.user.model.entity.enums.Role;
import com.dain_review.global.type.JwtOptionType;
import com.dain_review.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class RequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.getAccessTokenFromRequest(request);
        String refreshToken = jwtUtil.getRefreshTokenFromRequest(request);
        Claims info;

        if (!StringUtils.hasText(accessToken) || !StringUtils.hasText(refreshToken)) {
            jwtUtil.accessTokenCookieClear(request, response);
            jwtUtil.refreshTokenCookieClear(request, response);
            jwtUtil.jwtExceptionHandler(response, AuthErrorCode.NOT_FOUND_TOKEN);
            return;
        }

        //accessToken 검증 X
        if (!jwtUtil.validateToken(accessToken, response)) {
            jwtUtil.accessTokenCookieClear(request, response);
            //accessToken 검증 X / refreshToken 검증 X
            if (!jwtUtil.validateToken(refreshToken, response)) {
                jwtUtil.refreshTokenCookieClear(request, response);
                jwtUtil.jwtExceptionHandler(response, AuthErrorCode.JWT_EMPTY);
                return;
            }

            //accessToken 검증 X / refreshToken 검증 O
            info = jwtUtil.getUserInfoFromToken(refreshToken, response);
            response.addCookie(jwtUtil.getAccessTokenCookie((String) info.get(JwtOptionType.EMAIL.name()), (String) info.get(JwtOptionType.ROLE.name())));
        }else{
            info = jwtUtil.getUserInfoFromToken(accessToken, response);
        }

        setAuthentication(info.getSubject(), info.get(JwtOptionType.ROLE.name()).toString());
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String email, String role) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email, role);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String email, String role) {
        CustomUserDetails userDetails = new CustomUserDetails(null, email, null, Role.valueOf(role));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}

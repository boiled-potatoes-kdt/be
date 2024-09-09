package com.dain_review.domain.user.config.filter;


import com.dain_review.domain.user.config.model.CustomUserDetails;
import com.dain_review.domain.user.model.entity.enums.Role;
import com.dain_review.global.type.JwtOptionType;
import com.dain_review.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class RequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = jwtUtil.getAccessTokenFromRequest(request);
        String refreshToken = jwtUtil.getRefreshTokenFromRequest(request);
        Claims info;

        if (!StringUtils.hasText(accessToken) || !StringUtils.hasText(refreshToken)) {
            jwtUtil.accessTokenCookieClear(request, response);
            jwtUtil.refreshTokenCookieClear(request, response);
            filterChain.doFilter(request, response);
            return;
        }

        // accessToken 검증 X
        if (!jwtUtil.validateToken(accessToken, response)) {
            jwtUtil.accessTokenCookieClear(request, response);
            // accessToken 검증 X / refreshToken 검증 X
            if (!jwtUtil.validateToken(refreshToken, response)) {
                jwtUtil.refreshTokenCookieClear(request, response);
                filterChain.doFilter(request, response);
                return;
            }

            // accessToken 검증 X / refreshToken 검증 O
            info = jwtUtil.getUserInfoFromToken(refreshToken, response);
            response.addCookie(
                    jwtUtil.getAccessTokenCookie(
                            (String) info.get(JwtOptionType.EMAIL.name()),
                            (String) info.get(JwtOptionType.ROLE.name()),
                            (Long) info.get(JwtOptionType.USER_ID.name())));
        } else {
            info = jwtUtil.getUserInfoFromToken(accessToken, response);
        }

        Long userId =
                info.get(JwtOptionType.USER_ID.name()) instanceof Integer
                        ? ((Integer) info.get(JwtOptionType.USER_ID.name())).longValue()
                        : (Long) info.get(JwtOptionType.USER_ID.name());

        setAuthentication(info.getSubject(), (String) info.get(JwtOptionType.ROLE.name()), userId);
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String email, String role, Long userId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email, role, userId);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String email, String role, Long userId) {
        CustomUserDetails userDetails =
                new CustomUserDetails(userId, email, null, Role.valueOf(role));
        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
    }

}

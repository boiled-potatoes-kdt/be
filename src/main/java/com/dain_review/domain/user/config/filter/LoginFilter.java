package com.dain_review.domain.user.config.filter;

import com.dain_review.domain.user.config.model.CustomUserDetails;
import com.dain_review.domain.user.exception.AuthException;
import com.dain_review.domain.user.exception.errortype.AuthErrorCode;
import com.dain_review.domain.user.model.request.LoginRequest;
import com.dain_review.global.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public LoginFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {

        try {
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password(),
                            null
                    )
            );
        } catch (IOException e) {
            throw new AuthException(AuthErrorCode.LOGIN_ERROR);
        }

    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException {
        CustomUserDetails userDetails = ((CustomUserDetails) authResult.getPrincipal());
        String email = userDetails.getEmail();
        String role = userDetails.getUserRole().name();

        response.addCookie(jwtUtil.getAccessTokenCookie(email, role));
        response.addCookie(jwtUtil.getRefreshTokenCookie(email, role));

        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        writer.flush();
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) {
        jwtUtil.jwtExceptionHandler(response, AuthErrorCode.LOGIN_ERROR);
    }

}

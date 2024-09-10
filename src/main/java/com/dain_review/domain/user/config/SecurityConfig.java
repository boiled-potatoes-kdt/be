package com.dain_review.domain.user.config;


import com.dain_review.domain.auth.client.GoogleApiClient;
import com.dain_review.domain.auth.client.KakaoApiClient;
import com.dain_review.domain.auth.client.NaverApiClient;
import com.dain_review.domain.user.config.filter.*;
import com.dain_review.domain.user.exception.AuthException;
import com.dain_review.domain.user.exception.errortype.AuthErrorCode;
import com.dain_review.global.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final KakaoApiClient kakaoApiClient;
    private final GoogleApiClient googleApiClient;
    private final NaverApiClient naverApiClient;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http, AuthenticationConfiguration authenticationConfiguration)
            throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        configurer ->
                                configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(
                matcher ->
                        matcher.requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/login/oauth2/code/kakao").permitAll()
                                .requestMatchers(HttpMethod.POST, "/login/oauth2/code/naver").permitAll()
                                .requestMatchers(HttpMethod.POST, "/login/oauth2/code/google").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/post/notices/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                                .anyRequest().authenticated()
        );

        // TODO: Front 주소 확정시
        //        http.cors(httpSecurityCorsConfigurer ->
        // httpSecurityCorsConfigurer.configurationSource(
        //                corsConfigurationSource()));

        http.logout(
                logout ->
                        logout.logoutUrl("/logout")
                                .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                                .permitAll());

        http.addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(requestFilter(), LoginFilter.class);

        http.addFilterBefore(kakaoLoginFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(googleLoginFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(naverLoginFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter filter = new LoginFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        // 필터 등록 로그 추가
        System.out.println("LoginFilter registered");
        return filter;
    }

    @Bean
    public KakaoLoginFilter kakaoLoginFilter() throws Exception {
        KakaoLoginFilter filter = new KakaoLoginFilter(jwtUtil, kakaoApiClient);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public GoogleLoginFilter googleLoginFilter() throws Exception {
        GoogleLoginFilter filter = new GoogleLoginFilter(jwtUtil, googleApiClient);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public NaverLoginFilter naverLoginFilter() throws Exception {
        NaverLoginFilter filter = new NaverLoginFilter(jwtUtil, naverApiClient);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public RequestFilter requestFilter() {
        return new RequestFilter(jwtUtil);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    // TODO: 프론트 주소가 결정되면 CORS 설정
    //    @Bean
    //    public CorsConfigurationSource corsConfigurationSource() {
    //        CorsConfiguration config = new CorsConfiguration();
    //
    //        config.setAllowCredentials(true);
    //        config.setAllowedOrigins(List.of("https://localhost:8080", "http://localhost:8080",
    // "https://sandbox-pay.nicepay.co.kr/", "https://pay.nicepay.co.kr/"));
    //        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
    //        config.setAllowedHeaders(List.of("*"));
    //        config.setExposedHeaders(List.of("*"));
    //
    //        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //        source.registerCorsConfiguration("/**", config);
    //        return source;
    //    }
    //

    class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

        @Override
        public void onLogoutSuccess(
                HttpServletRequest request,
                HttpServletResponse response,
                Authentication authentication)
                throws IOException, ServletException {
            try {

                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (jwtUtil.validateCookieName(cookie)) {
                            cookie.setValue("");
                            cookie.setPath("/");
                            cookie.setMaxAge(0);
                            response.addCookie(cookie);
                        }
                    }
                }

            } catch (Exception e) {
                throw new AuthException(AuthErrorCode.LOGOUT_ERROR);
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect("/");
        }
    }
}

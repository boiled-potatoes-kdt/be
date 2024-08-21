package com.dain_review.global.util;

import com.dain_review.domain.user.exception.AuthException;
import com.dain_review.domain.user.exception.errortype.AuthErrorCode;
import com.dain_review.global.exception.GlobalResponse;
import com.dain_review.global.type.JwtOptionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {


    @Value("${jwt.key}")
    private String secretKey;

    private static final String AUTHORIZATION_ACCESS_HEADER = "accessToken";
    private static final String AUTHORIZATION_REFRESH_HEADER = "refreshToken";
    private static final String BEARER_PREFIX = "Bearer ";

    private final long ACCESS_TOKEN_EXPIRATION_PERIOD = 30 * 60 * 1000L; // 30분
    private final long REFRESH_TOKEN_EXPIRATION_PERIOD = 14 * 24 * 60 * 60 * 1000L; // 14일

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;


    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);

    }

    public void jwtExceptionHandler(HttpServletResponse response, AuthErrorCode error) {
        response.setStatus(error.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(new GlobalResponse(error.getMsg(), error.getStatus()));
            response.getWriter().write(json);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public Cookie getAccessTokenCookie(String email, String role) throws UnsupportedEncodingException {
        return getCookie(createAccessToken(email, role), AUTHORIZATION_ACCESS_HEADER, ACCESS_TOKEN_EXPIRATION_PERIOD);
    }

    public Cookie getRefreshTokenCookie(String email, String role) throws UnsupportedEncodingException {
        return getCookie(createAccessToken(email, role), AUTHORIZATION_REFRESH_HEADER, REFRESH_TOKEN_EXPIRATION_PERIOD);
    }

    public boolean validateCookieName(Cookie cookie) {
        return cookie.getName() == AUTHORIZATION_ACCESS_HEADER || cookie.getName() == AUTHORIZATION_REFRESH_HEADER;
    }

    public boolean validateToken(String token, HttpServletResponse response) {
        try {
            token = substringToken(token, response);
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            jwtExceptionHandler(response, AuthErrorCode.INVALID_JWT_SIGNATURE);
        } catch (ExpiredJwtException e) {
            jwtExceptionHandler(response, AuthErrorCode.EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            jwtExceptionHandler(response, AuthErrorCode.UNSUPPORTED_JWT);
        } catch (IllegalArgumentException e) {
            jwtExceptionHandler(response, AuthErrorCode.JWT_EMPTY);
        }
        return false;
    }


    private String substringToken(String tokenValue, HttpServletResponse response) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(BEARER_PREFIX.length());
        }
        jwtExceptionHandler(response, AuthErrorCode.NOT_FOUND_TOKEN);
        return "";
    }

    private String createAccessToken(String email, String role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email)
                        .claim(JwtOptionType.ROLE.name(), role)
                        .claim(JwtOptionType.EMAIL.name(), email)
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_EXPIRATION_PERIOD))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    private String createRefreshToken(String email, String role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email)
                        .claim(JwtOptionType.ROLE.name(), role)
                        .claim(JwtOptionType.EMAIL.name(), email)
                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_EXPIRATION_PERIOD))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    private Cookie getCookie(String token, String authorizationRefreshHeader, long refreshTokenExpirationPeriod) throws UnsupportedEncodingException {
        token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");
        Cookie cookie = new Cookie(authorizationRefreshHeader, token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "None");
        cookie.setMaxAge((int) refreshTokenExpirationPeriod);

        return cookie;
    }


}

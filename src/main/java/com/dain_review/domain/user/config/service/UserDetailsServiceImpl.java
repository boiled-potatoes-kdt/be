package com.dain_review.domain.user.config.service;


import com.dain_review.domain.user.config.model.CustomUserDetails;
import com.dain_review.domain.user.exception.AuthException;
import com.dain_review.domain.user.exception.errortype.AuthErrorCode;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new AuthException(AuthErrorCode.LOGIN_ERROR));

        return new CustomUserDetails(
                user.getId(), user.getEmail(), user.getPassword(), user.getRole());
    }
}

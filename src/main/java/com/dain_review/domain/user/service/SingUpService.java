package com.dain_review.domain.user.service;


import com.dain_review.domain.user.exception.RegisterException;
import com.dain_review.domain.user.exception.errortype.RegisterErrorCode;
import com.dain_review.domain.user.model.request.EmailCheckRequest;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.api.API;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SingUpService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ResponseEntity emailCheck(EmailCheckRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent())
            throw new RegisterException(RegisterErrorCode.EMAIL_SAME);
        return API.OK();
    }
}

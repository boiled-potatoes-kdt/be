package com.dain_review.domain.user.service;


import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.model.request.EnterpriserChangeRequest;
import com.dain_review.domain.user.model.request.EnterpriserExtraRegisterRequest;
import com.dain_review.domain.user.model.response.EnterpriserChangeResponse;
import com.dain_review.domain.user.model.response.EnterpriserResponse;
import com.dain_review.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnterpriserService {

    private final UserRepository userRepository;

    @Transactional
    public void signUpExtra(
            Long id, EnterpriserExtraRegisterRequest enterpriserExtraRegisterRequest) {

        User user = userRepository.getUserById(id);
        user.change(enterpriserExtraRegisterRequest);
    }

    public EnterpriserResponse getMyPage(Long id) {

        User user = userRepository.getUserById(id);
        return EnterpriserResponse.from(user);
    }

    public EnterpriserChangeResponse getChangePage(Long id) {

        User user = userRepository.getUserById(id);
        return EnterpriserChangeResponse.from(user);
    }

    @Transactional
    public EnterpriserChangeResponse changeInformation(
            EnterpriserChangeRequest enterpriserChangeRequest, Long id) {

        User user = userRepository.getUserById(id);
        user.change(enterpriserChangeRequest);

        return EnterpriserChangeResponse.from(user);
    }
}

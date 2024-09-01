package com.dain_review.domain.user.service;


import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.model.request.InfluencerChangeRequest;
import com.dain_review.domain.user.model.request.InfluencerExtraRegisterRequest;
import com.dain_review.domain.user.model.response.InfluencerChangeResponse;
import com.dain_review.domain.user.model.response.InfluencerResponse;
import com.dain_review.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InfluencerService {

    private final UserRepository userRepository;

    @Transactional
    public void signUpExtra(
            Long id, InfluencerExtraRegisterRequest influencerExtraRegisterRequest) {

        User user = userRepository.getUserById(id);
        user.change(influencerExtraRegisterRequest);
    }

    @Transactional
    public InfluencerResponse getMyPage(Long id) {

        User user = userRepository.getUserById(id);
        return InfluencerResponse.from(user);
    }

    // 인플루언서 회원정보 변경 페이지
    @Transactional
    public InfluencerChangeResponse getChangePage(Long id) {

        User user = userRepository.getUserById(id);
        return InfluencerChangeResponse.from(user);
    }

    // 인플루언서 회원정보 변경
    @Transactional
    public InfluencerChangeResponse changeInformation(
            InfluencerChangeRequest influencerChangeRequest, Long id) {

        User user = userRepository.getUserById(id);
        user.change(influencerChangeRequest);

        return InfluencerChangeResponse.from(user);
    }
}

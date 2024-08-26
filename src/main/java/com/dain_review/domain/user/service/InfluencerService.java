package com.dain_review.domain.user.service;


import com.dain_review.domain.user.exception.UserErrorCode;
import com.dain_review.domain.user.exception.UserException;
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

    // 인플루언서 회원가입 추가정보 입력
    @Transactional
    public void save(Long id, InfluencerExtraRegisterRequest influencerExtraRegisterRequest) {

        // 해당 아이디의 유저 조회
        User user = getUser(id);

        // 추가정보 값 추가
        user.change(influencerExtraRegisterRequest);
    }

    // 인플루언서 마이페이지
    @Transactional
    public InfluencerResponse get(Long id) {

        User user = getUser(id);

        // InfluencerResponse 로 변환
        return user.toInfluencerResponse();
    }

    // 인플루언서 회원정보 변경 페이지
    @Transactional
    public InfluencerChangeResponse getChange(Long id) {

        // 해당 아이디의 사용자 조회
        User user = getUser(id);

        // InfluencerChangeResponse 로 변환
        return user.toInfluencerChangeResponse();
    }

    // 인플루언서 회원정보 변경
    @Transactional
    public InfluencerChangeResponse change(
            InfluencerChangeRequest influencerChangeRequest, Long id) {

        // 해당 아이디의 유저 조회
        User user = getUser(id);

        // Todo - 이전비밀번호 일치하는지 확인

        // 변경내용 업데이트
        user.change(influencerChangeRequest);

        // InfluencerChangeResponse 로 변환
        return user.toInfluencerChangeResponse();
    }

    public User getUser(Long id) {
        // 해당 아이디의 유저 조회
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_BY_ID));
    }
}

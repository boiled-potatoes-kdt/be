package com.dain_review.domain.user.service;


import com.dain_review.domain.user.exception.UserErrorCode;
import com.dain_review.domain.user.exception.UserException;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.model.request.ProfileChangeRequest;
import com.dain_review.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void update(Long id, ProfileChangeRequest profileChangeRequest) {

        // 해당 아이디의 사용자 조회
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_BY_ID));

        // 프로필 주소 변경
        user.change(profileChangeRequest.profileImage());
    }

    @Transactional
    public void delete(Long id) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_BY_ID));

        // 사업주가 체험단을 등록했거나 인플루언서가 체험단을 신청 중일 때는 탈퇴가 불가능
        if (user.getCampaignList().size() != 0 || user.getApplicationList().size() != 0) {
            throw new RuntimeException();
        }

        user.delete();
    }
}

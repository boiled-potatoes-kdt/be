package com.dain_review.domain.user.service;


import com.dain_review.domain.user.exception.RegisterException;
import com.dain_review.domain.user.exception.errortype.RegisterErrorCode;
import com.dain_review.domain.user.model.entity.Influencer;
import com.dain_review.domain.user.model.entity.Sns;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.model.entity.enums.Gender;
import com.dain_review.domain.user.model.entity.enums.Role;
import com.dain_review.domain.user.model.entity.enums.SnsType;
import com.dain_review.domain.user.model.request.InfluencerChangeRequest;
import com.dain_review.domain.user.model.request.InfluencerExtraRegisterRequest;
import com.dain_review.domain.user.model.request.InfluencerSingUpRequest;
import com.dain_review.domain.user.model.response.InfluencerChangeResponse;
import com.dain_review.domain.user.model.response.InfluencerResponse;
import com.dain_review.domain.user.repository.InfluencerRepository;
import com.dain_review.domain.user.repository.SnsRepository;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.api.API;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Certification;
import com.siot.IamportRestClient.response.IamportResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class InfluencerService {

    private final IamportClient iamportClient;
    private final UserRepository userRepository;
    private final InfluencerRepository influencerRepository;
    private final SnsRepository snsRepository;
    private final PasswordEncoder pe;

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

    @Transactional
    public ResponseEntity singUpInfluencer(InfluencerSingUpRequest request) {
        try {
            IamportResponse<Certification> certification = iamportClient.certificationByImpUid(request.impId());

            if (certification.getResponse().getName().equals(request.name()))
                throw new RegisterException(RegisterErrorCode.FAIL_IMP_NAME_NOT_SAME);

            if (userRepository.findByEmail(request.email()).isPresent())
                throw new RegisterException(RegisterErrorCode.EMAIL_SAME);

            User user = userRepository.save(User.builder()
                    .email(request.email())
                    .role(Role.ROLE_INFLUENCER)
                    .marketing(request.marketing())
                    .isDeleted(false)
                    .phone(certification.getResponse().getPhone())
                    .point(0L)
                    .nickname(request.nickname())
                    .name(request.name())
                    .password(pe.encode(request.password()))
                    .joinPath(request.joinPath())
                    .build());
            Influencer influencer = influencerRepository.save(Influencer.builder()
                    .user(user)
                    .gender(Gender.valueOf(certification.getResponse().getGender().toUpperCase()))
                    .birthday(certification.getResponse().getBirth().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate())
                    .build());

            for (SnsType snsType : request.sns()) {
                snsRepository.save(Sns.builder()
                        .snsType(snsType)
                        .influencer(influencer)
                        .build());
            }


        } catch (IamportResponseException e) {
            throw new RegisterException(RegisterErrorCode.FAIL_IMP_ID);
        } catch (IOException e) {
            throw new RegisterException(RegisterErrorCode.FAIL_IMP_ID);
        }

        return API.OK();
    }

}

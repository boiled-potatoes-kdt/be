package com.dain_review.domain.user.service;


import com.dain_review.domain.user.exception.RegisterException;
import com.dain_review.domain.user.exception.errortype.RegisterErrorCode;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.model.request.FindUserIdRequest;
import com.dain_review.domain.user.model.request.UserPasswordChangeRequest;
import com.dain_review.domain.user.model.response.FindUserIdResponse;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.api.API;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Certification;
import com.siot.IamportRestClient.response.IamportResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindUserService {

    private final IamportClient iamportClient;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ResponseEntity findUserId(FindUserIdRequest request) {
        try {
            IamportResponse<Certification> certification =
                    iamportClient.certificationByImpUid(request.impId());

            if (certification.getResponse().getName().equals(request.name()))
                throw new RegisterException(RegisterErrorCode.FAIL_IMP_NAME_NOT_SAME);

            User user =
                    userRepository
                            .findByNameAndPhone(
                                    request.name(), certification.getResponse().getPhone())
                            .orElseThrow(
                                    () -> new RegisterException(RegisterErrorCode.NOT_FOUND_USER));

            return API.OK(new FindUserIdResponse(user.getEmail()));
        } catch (IamportResponseException e) {
            throw new RegisterException(RegisterErrorCode.FAIL_IMP_ID);
        } catch (IOException e) {
            throw new RegisterException(RegisterErrorCode.FAIL_IMP_ID);
        }
    }

    // TODO: 예비군 갔다와서 프론트랑 이야기 후 작업.
    @Transactional
    public ResponseEntity passwordChange(UserPasswordChangeRequest request) {
        try {
            IamportResponse<Certification> certification =
                    iamportClient.certificationByImpUid(request.impId());

            if (certification.getResponse().getName().equals(request.name()))
                throw new RegisterException(RegisterErrorCode.FAIL_IMP_NAME_NOT_SAME);

        } catch (IamportResponseException e) {
            throw new RegisterException(RegisterErrorCode.FAIL_IMP_ID);
        } catch (IOException e) {
            throw new RegisterException(RegisterErrorCode.FAIL_IMP_ID);
        }

        return API.OK();
    }
}

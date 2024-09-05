package com.dain_review.domain.review.service;

import com.dain_review.domain.Image.entity.enums.ContentType;
import com.dain_review.domain.Image.service.ImageFileService;
import com.dain_review.domain.application.exception.ApplicationException;
import com.dain_review.domain.application.exception.errortype.ApplicationErrorCode;
import com.dain_review.domain.application.model.entity.Application;
import com.dain_review.domain.application.repository.ApplicationRepository;
import com.dain_review.domain.review.model.entity.Review;
import com.dain_review.domain.review.model.request.ReviewRequest;
import com.dain_review.domain.review.repository.ReviewRepository;
import com.dain_review.domain.user.exception.UserException;
import com.dain_review.domain.user.exception.errortype.UserErrorCode;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.type.S3PathPrefixType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final ImageFileService imageFileService;

    public void createReview(Long userId, Long applicationId, ReviewRequest reviewRequest, List<MultipartFile> imageFiles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorCode.NOT_FOUND_BY_ID));
        application.checkReviewCreateAble(userId);

        Review review = Review.builder()
                .user(user)
                .campaign(application.getCampaign())
                .application(application)
                .url(reviewRequest.url())
                .build();
        reviewRepository.save(review);

        imageFileService.saveImageFiles(imageFiles, ContentType.REVIEW, review.getId(), S3PathPrefixType.S3_REVIEW_IMAGE_PATH);
    }
}

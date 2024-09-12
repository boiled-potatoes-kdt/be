package com.dain_review.domain.user.service;


import com.dain_review.domain.Image.service.ImageFileService;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.type.S3PathPrefixType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ImageFileService imageFileService;

    @Transactional
    public void update(Long id, MultipartFile imageFile) {

        User user = userRepository.getUserById(id);

        // 이미지 처리 로직을 ImageService로 위임
        String imageFileName =
                imageFileService.validateAndUploadImage(
                        imageFile, S3PathPrefixType.S3_PROFILE_IMAGE_PATH);
        String imageUrl =
                imageFileService.selectImage(imageFileName, S3PathPrefixType.S3_PROFILE_IMAGE_PATH);

        user.change(imageFileName, imageUrl);
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.getUserById(id);
        user.delete();
    }
}

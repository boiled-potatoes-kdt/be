package com.dain_review.domain.Image.service;


import com.dain_review.domain.Image.entity.ImageFile;
import com.dain_review.domain.Image.exception.S3Exception;
import com.dain_review.domain.Image.exception.errortype.S3ErrorCode;
import com.dain_review.domain.Image.repository.ImageFileRepository;
import com.dain_review.domain.Image.util.ImageFileValidUtil;
import com.dain_review.domain.campaign.exception.CampaignException;
import com.dain_review.domain.campaign.exception.errortype.CampaignErrorCode;
import com.dain_review.domain.post.model.entity.Post;
import com.dain_review.global.type.S3PathPrefixType;
import com.dain_review.global.util.S3Util;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageFileService {

    private final ImageFileRepository imageFileRepository;
    private final S3Util s3Util;

    public String uploadImage(MultipartFile imageFile, S3PathPrefixType pathPrefixType) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new CampaignException(CampaignErrorCode.IMAGE_REQUIRED);
        }
        if (!ImageFileValidUtil.isValidImageFile(imageFile)) {
            throw new S3Exception(S3ErrorCode.INVALID_IMAGE_FILE);
        }
        return s3Util.saveImage(imageFile, pathPrefixType.toString()).join();
    }

    public String getImageUrl(String imageName, S3PathPrefixType pathPrefixType) {
        return s3Util.selectImage(imageName, pathPrefixType.toString());
    }

    public String selectImage(String imageUrl, S3PathPrefixType pathPrefixType) {
        return s3Util.selectImage(imageUrl, pathPrefixType.toString());
    }

    public void saveImageFiles(
            List<MultipartFile> imageFiles, Post post, S3PathPrefixType s3PathPrefixType) {
        if (imageFiles == null || imageFiles.isEmpty()) {
            return;
        }

        for (MultipartFile imageFile : imageFiles) {
            if (imageFile != null && !imageFile.isEmpty()) {
                if (!ImageFileValidUtil.isValidImageFile(imageFile)) {
                    throw new S3Exception(S3ErrorCode.INVALID_IMAGE_FILE);
                }

                String fileName = s3Util.saveImage(imageFile, s3PathPrefixType.toString()).join();
                ImageFile file = ImageFile.builder().post(post).fileName(fileName).build();
                imageFileRepository.save(file);
            }
        }
    }

    public void deleteImageFiles(List<String> fileNames, S3PathPrefixType s3PathPrefixType) {
        for (String fileName : fileNames) {
            s3Util.deleteImage(fileName, s3PathPrefixType.toString());
            ImageFile file = imageFileRepository.findByFileName(fileName);
            imageFileRepository.delete(file);
        }
    }

    public List<String> findImageUrls(Long postId, S3PathPrefixType s3PathPrefixType) {
        List<ImageFile> imageFiles = imageFileRepository.findByPostId(postId);
        return imageFiles.stream()
                .map(it -> s3Util.selectImage(it.getFileName(), s3PathPrefixType.toString()))
                .collect(Collectors.toList());
    }

    public String getUserProfileUrl(String userProfileImageName) {
        return s3Util.selectImage(
                userProfileImageName, S3PathPrefixType.S3_PROFILE_IMAGE_PATH.toString());
    }
}

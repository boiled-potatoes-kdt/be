package com.dain_review.global.util;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.dain_review.global.model.request.ImageFileRequest;
import com.dain_review.global.util.error.S3Exception;
import com.dain_review.global.util.errortype.S3ErrorCode;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class S3Util {

    @Value("${cloud.aws.s3.bucket}") private String bucketName;

    private final AmazonS3 s3Client;

    /**
     * 이미지 업로드
     *
     * @param imageFileRequest 업로드 할 이미지 파일
     * @return S3에 업로드된 파일 이름 반환
     */
    @Async("S3PoolTask")
    public CompletableFuture<String> saveImage(ImageFileRequest imageFileRequest) {
        MultipartFile file = imageFileRequest.getFile();
        String fileName =
                String.valueOf(System.currentTimeMillis()) + "." + extractExtensionName(file);
        try {
            s3Client.putObject(bucketName, fileName, file.getInputStream(), null);
        } catch (IOException e) {
            throw new S3Exception(S3ErrorCode.IMAGE_UPLOAD_FAILED);
        }
        return CompletableFuture.completedFuture(fileName);
    }

    /**
     * 이미지 조회
     *
     * @param fileName 조회할 이미지 확장자 포함 이름
     * @return fileName 과 일치하는 이름의 이미지 url 반환
     */
    public String selectImage(String fileName) {
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, fileName));
        URL url = s3Client.getUrl(bucketName, fileName);
        return url.toString();
    }

    /**
     * 이미지 삭제
     *
     * @param fileName 삭제할 이미지 확장자 포함 이름
     */
    public void deleteImage(String fileName) {
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }

    private String extractExtensionName(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String[] div = fileName.split("\\.");
        return div[div.length - 1];
    }
}

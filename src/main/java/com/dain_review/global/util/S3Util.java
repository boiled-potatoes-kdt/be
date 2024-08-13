package com.dain_review.global.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class S3Util {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 s3Client;

    /**
     * 이미지 업로드
     * @param file  업로드 할 이미지 파일
     * @return      S3에 업로드된 파일의 URL 반환
     */
    public String uploadFile(MultipartFile file) {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try {
            s3Client.putObject(bucketName, fileName, file.getInputStream(), null);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
        return s3Client.getUrl(bucketName, fileName).toString();
    }

    /**
     * 이미지 조회
     * @param fileName  조회할 이미지 확장자 포함 이름
     * @return          조회 이미지 url
     */
    public InputStream getImage(String fileName) {
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, fileName));
        return s3Object.getObjectContent();
    }

    /**
     * 이미지 삭제
     * @param fileName 삭제할 이미지 확장자 포함 이름
     */
    public void deleteImage(String fileName) {
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }
}
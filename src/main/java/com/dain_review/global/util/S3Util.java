package com.dain_review.global.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.dain_review.global.util.error.S3Exception;
import com.dain_review.global.util.errortype.S3ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.dain_review.global.model.request.ImageFileRequest;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class S3Util {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 s3Client;

    //TODO: 비동기 -> String return 늦어질수도있어서. 이 부분은 테스트 꼭 하세요! 여러 이미지를 등록하는 매서드 만들고 실제로 테스트 해서 String 값이 잘 나오는지 확인
    
    /**
     * 이미지 업로드
     * @param imageFileRequest  업로드 할 이미지 파일
     * @return                  S3에 업로드된 파일의 URL 반환
     */
//    @Async("S3PoolTask")
    public String saveImage(ImageFileRequest imageFileRequest){
        MultipartFile file = imageFileRequest.getFile();
        String fileName = String.valueOf(System.currentTimeMillis());
        try {
            s3Client.putObject(bucketName, fileName, file.getInputStream(), null);
        } catch (IOException e) {
            throw new S3Exception(S3ErrorCode.IMAGE_UPLOAD_FAILED);
        }
        return fileName;
    }

    /**
     * 이미지 조회
     * @param fileName  조회할 이미지 확장자 포함 이름
     * @return          조회 이미지 url
     */
    public String selectImage(String fileName) {
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, fileName));
        return s3Object.getObjectContent().toString();
    }

    /**
     * 이미지 삭제
     * @param fileName 삭제할 이미지 확장자 포함 이름
     */
    public void deleteImage(String fileName) {
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }

}
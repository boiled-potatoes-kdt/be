package com.dain_review.global.util;

import com.amazonaws.services.s3.AmazonS3;
import com.dain_review.global.model.request.ImageFileRequest;
import com.dain_review.global.util.error.S3Exception;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class S3UtilTest {

    @Value("${cloud.aws.s3.bucket}") private String bucketName;

    @Mock
    AmazonS3 s3Client;

    @InjectMocks
    S3Util s3Util;

}
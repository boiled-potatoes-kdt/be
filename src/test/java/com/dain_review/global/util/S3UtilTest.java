package com.dain_review.global.util;

import com.amazonaws.services.s3.AmazonS3;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;

class S3UtilTest {

    @Value("${cloud.aws.s3.bucket}") private String bucketName;

    @Mock
    AmazonS3 s3Client;

    @InjectMocks
    S3Util s3Util;

}
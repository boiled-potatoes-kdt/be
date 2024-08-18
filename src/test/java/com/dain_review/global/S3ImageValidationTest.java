package com.dain_review.global;

import com.amazonaws.services.s3.AmazonS3;
import com.dain_review.global.util.S3Util;
import com.dain_review.global.model.request.ImageFileRequest;
import com.dain_review.global.util.error.S3Exception;
import com.dain_review.global.util.errortype.S3ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class S3ImageValidationTest {

    @Mock
    private AmazonS3 s3Client;

    @InjectMocks
    private S3Util s3Util;

    private LocalValidatorFactoryBean validator;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        // 리플렉션을 사용해 bucketName 필드에 값을 설정합니다.
        Field bucketNameField = S3Util.class.getDeclaredField("bucketName");
        bucketNameField.setAccessible(true);
        bucketNameField.set(s3Util, "my-test-bucket");  // 테스트용 버킷 이름 설정
    }

    @Test
    public void 유효한_이미지_형식으로_업로드() {
        // Given
        MultipartFile mockFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[10]);
        ImageFileRequest imageFileRequest = new ImageFileRequest(mockFile);

        // Validate
        BindingResult bindingResult = new BeanPropertyBindingResult(imageFileRequest, "imageFileRequest");
        validator.validate(imageFileRequest, bindingResult);
        assertFalse(bindingResult.hasErrors());

        // When
        s3Util.saveImage(imageFileRequest);

        // Then
        verify(s3Client, times(1)).putObject(eq("my-test-bucket"), anyString(), any(), isNull());
    }

    @Test
    public void 유효하지_않은_이미지_형식으로_업로드() {
        // Given
        MultipartFile mockFile = new MockMultipartFile("file", "document.txt", "text/plain", new byte[10]);
        ImageFileRequest imageFileRequest = new ImageFileRequest(mockFile);

        // Validate
        BindingResult bindingResult = new BeanPropertyBindingResult(imageFileRequest, "imageFileRequest");
        validator.validate(imageFileRequest, bindingResult);
        assertTrue(bindingResult.hasErrors());

        // Expect an exception
        assertThrows(S3Exception.class, () -> {
            // When
            s3Util.saveImage(imageFileRequest);
        });

        verify(s3Client, times(0)).putObject(anyString(), anyString(), any(), isNull());
    }
}


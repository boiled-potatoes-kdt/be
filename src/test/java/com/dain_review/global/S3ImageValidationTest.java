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
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class S3ImageValidationTest {

    @Mock
    private AmazonS3 s3Client;

    @InjectMocks
    private S3Util s3Util;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void 유효한_이미지_형식으로_업로드() {
        // Given
        MultipartFile mockFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[10]);
        ImageFileRequest imageFileRequest = new ImageFileRequest(mockFile);

        // When
        String result = s3Util.saveImage(imageFileRequest);

        // Then
        assertNotNull(result);
        verify(s3Client, times(1)).putObject(anyString(), anyString(), any(), isNull());
    }

    @Test
    public void 유효하지_않은_이미지_형식으로_업로드() {
        // Given
        MultipartFile mockFile = new MockMultipartFile("file", "document.txt", "text/plain", new byte[10]);
        ImageFileRequest imageFileRequest = new ImageFileRequest(mockFile);

        // Then
        S3Exception exception = assertThrows(S3Exception.class, () -> {
            // When
            s3Util.saveImage(imageFileRequest);
        });

        assertEquals(S3ErrorCode.IMAGE_UPLOAD_FAILED, exception.getErrorCode());
        verify(s3Client, times(0)).putObject(anyString(), anyString(), any(), isNull());
    }
}

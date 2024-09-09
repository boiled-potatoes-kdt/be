package com.dain_review.global;

import com.amazonaws.services.s3.AmazonS3;
import com.dain_review.global.util.S3Util;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class S3AsyncTest {

    private S3Util s3Util;
    private AmazonS3 s3Client;
    private String bucketName = "my-test-bucket";
    private AsyncTaskExecutor asyncTaskExecutor;

    @BeforeEach
    void setUp() {
        s3Client = mock(AmazonS3.class);
        asyncTaskExecutor = new ThreadPoolTaskExecutor();
    }

/*    @Test
    void 이미지_비동기_업로드() throws ExecutionException, InterruptedException, IOException {
        // Given
        MultipartFile mockFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[10]);
        ImageFileRequest imageFileRequest = new ImageFileRequest(mockFile);

        // When
        s3Util.saveImage(imageFileRequest);

        // Then
//        assertNotNull(result);
//        assertEquals(result.get, fileName); // 실제 비동기 실행 결과 확인

        ArgumentCaptor<String> bucketCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> fileCaptor = ArgumentCaptor.forClass(String.class);
        verify(s3Client, times(1)).putObject(bucketCaptor.capture(), fileCaptor.capture(), any(InputStream.class), isNull());

        assertEquals(bucketName, bucketCaptor.getValue());
    }*/
}

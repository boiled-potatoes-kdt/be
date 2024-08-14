package com.dain_review;


import com.dain_review.global.model.request.ImageFileRequest;
import com.dain_review.global.util.S3Util;
import jakarta.validation.Valid;
import java.awt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class FileController {

    private final S3Util s3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@Valid ImageFileRequest imageFileRequest) {
        String fileUrl = s3Service.saveImage(imageFileRequest);
        return new ResponseEntity<>(fileUrl, HttpStatus.OK);
    }

    // 이미지 조회
    @GetMapping("/{fileName}")
    public ResponseEntity<String> getFile(@PathVariable String fileName) {
        String imageurl = s3Service.selectImage(fileName);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageurl);
    }

    // 이미지 삭제
    @DeleteMapping("/{fileName}")
    public ResponseEntity<Void> deleteImage(@PathVariable String fileName) {
        s3Service.deleteImage(fileName);
        return ResponseEntity.noContent().build();
    }
}

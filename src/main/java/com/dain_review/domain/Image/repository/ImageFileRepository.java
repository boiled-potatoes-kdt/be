package com.dain_review.domain.Image.repository;


import com.dain_review.domain.Image.entity.ImageFile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
    List<ImageFile> findByPostId(Long id);

    ImageFile findByFileName(String fileName);
}

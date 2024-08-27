package com.dain_review.domain.post.repository;

import com.dain_review.domain.post.model.entity.AttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachedFileRepository extends JpaRepository<AttachedFile, Long> {
    List<AttachedFile> findByPostId(Long id);

    AttachedFile findByFileName(String fileName);
}

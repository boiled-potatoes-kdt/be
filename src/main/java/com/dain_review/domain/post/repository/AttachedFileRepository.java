package com.dain_review.domain.post.repository;


import com.dain_review.domain.post.model.entity.AttachedFile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachedFileRepository extends JpaRepository<AttachedFile, Long> {
    List<AttachedFile> findByPostId(Long id);

    AttachedFile findByFileName(String fileName);
}

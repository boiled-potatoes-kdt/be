package com.dain_review.domain.review.repository;

import com.dain_review.domain.review.model.entity.ReviewAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewAttachedFileRepository extends JpaRepository<ReviewAttachedFile, Long> {
}

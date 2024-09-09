package com.dain_review.domain.review.repository;


import com.dain_review.domain.review.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {}

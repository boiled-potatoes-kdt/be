package com.dain_review.domain.post.repository;


import com.dain_review.domain.post.model.entity.PostMeta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostMetaRepository extends JpaRepository<PostMeta, Long> {}

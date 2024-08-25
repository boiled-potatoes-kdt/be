package com.dain_review.domain.user.repository;


import com.dain_review.domain.user.model.entity.Sns;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnsRepository extends JpaRepository<Sns, Long> {}

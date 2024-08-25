package com.dain_review.domain.user.repository;


import com.dain_review.domain.user.model.entity.Influencer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfluencerRepository extends JpaRepository<Influencer, Long> {}

package com.dain_review.domain.user.repository;


import com.dain_review.domain.user.model.entity.Enterpriser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnterpriserRepository extends JpaRepository<Enterpriser, Long> {}

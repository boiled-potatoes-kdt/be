package com.dain_review.domain.choice.repository;


import com.dain_review.domain.choice.model.entity.Choice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {}

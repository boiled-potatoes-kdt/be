package com.dain_review.domain.user.repository;


import com.dain_review.domain.user.model.entity.Enterpriser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnterpriserRepository extends JpaRepository<Enterpriser, Long> {

    Optional<Enterpriser> findById(Long id);
}

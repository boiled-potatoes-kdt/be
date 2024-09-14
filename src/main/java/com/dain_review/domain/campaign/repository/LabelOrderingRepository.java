package com.dain_review.domain.campaign.repository;


import com.dain_review.domain.campaign.exception.CampaignException;
import com.dain_review.domain.campaign.exception.errortype.CampaignErrorCode;
import com.dain_review.domain.campaign.model.entity.LabelOrdering;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelOrderingRepository extends JpaRepository<LabelOrdering, Long> {
    Optional<LabelOrdering> findByLabel(String label);

    // 기본 메서드 추가
    default LabelOrdering getLabelOrderingByLabel(String label) {
        return findByLabel(label)
                .orElseThrow(() -> new CampaignException(CampaignErrorCode.CANNOT_MANAGE_CAMPAIGN));
    }
}

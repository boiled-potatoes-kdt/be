package com.dain_review.global.model.response;


import com.dain_review.domain.campaign.model.response.CampaignSummaryResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PagedResponse<T> {
    private List<T> content;
    private Long totalElements;
    private int totalPages;

    public static PagedResponse<CampaignSummaryResponse> of(
            List<CampaignSummaryResponse> content, long totalElements, int totalPages) {
        return new PagedResponse<>(content, totalElements, totalPages);
    }
}

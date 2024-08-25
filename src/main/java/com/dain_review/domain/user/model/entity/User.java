package com.dain_review.domain.user.model.entity;


import com.dain_review.domain.application.model.entity.Application;
import com.dain_review.domain.application.model.entity.enums.State;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.user.model.entity.enums.Role;
import com.dain_review.domain.user.model.request.EnterpriserChangeRequest;
import com.dain_review.domain.user.model.request.EnterpriserExtraRegisterRequest;
import com.dain_review.domain.user.model.request.InfluencerChangeRequest;
import com.dain_review.domain.user.model.request.InfluencerExtraRegisterRequest;
import com.dain_review.domain.user.model.response.EnterpriserChangeResponse;
import com.dain_review.domain.user.model.response.EnterpriserResponse;
import com.dain_review.domain.user.model.response.InfluencerChangeResponse;
import com.dain_review.domain.user.model.response.InfluencerResponse;
import com.dain_review.domain.user.model.response.SnsResponse;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class User extends BaseEntity {

    private String name;
    private String email;
    private String nickname;
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Application> applicationList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Campaign> campaignList;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Influencer influencer;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Enterpriser enterpriser;

    private Long point;
    private String phone;
    private String joinPath;
    private String address;
    private String addressDetail;
    private String postalCode;
    private String profileImage;
    private Boolean marketing;
    private Boolean penalty;
    private Boolean isDeleted;

    public void change(EnterpriserChangeRequest enterpriserChangeRequest) {
        this.password = enterpriserChangeRequest.newPassword();
        this.name = enterpriserChangeRequest.name();
        this.nickname = enterpriserChangeRequest.nickname();
        this.phone = enterpriserChangeRequest.phone();
        this.address = enterpriserChangeRequest.address();
        this.addressDetail = enterpriserChangeRequest.addressDetail();
        this.postalCode = enterpriserChangeRequest.postalCode();
    }

    // 완료
    public void change(EnterpriserExtraRegisterRequest enterpriserExtraRegisterRequest) {
        this.profileImage = enterpriserExtraRegisterRequest.profileImage();
        this.address = enterpriserExtraRegisterRequest.address();
        this.addressDetail = enterpriserExtraRegisterRequest.addressDetail();
        this.postalCode = enterpriserExtraRegisterRequest.postalCode();
    }

    // 완료
    public void change(InfluencerExtraRegisterRequest influencerExtraRegisterRequest) {
        this.profileImage = influencerExtraRegisterRequest.profileImage();
        this.address = influencerExtraRegisterRequest.address();
        this.addressDetail = influencerExtraRegisterRequest.addressDetail();
        this.postalCode = influencerExtraRegisterRequest.postalCode();
        this.influencer.change(influencerExtraRegisterRequest);
    }

    public void change(InfluencerChangeRequest influencerChangeRequest) {
        this.password = influencerChangeRequest.newPassword();
        this.name = influencerChangeRequest.name();
        this.nickname = influencerChangeRequest.nickname();
        this.phone = influencerChangeRequest.phone();
        this.address = influencerChangeRequest.address();
        this.addressDetail = influencerChangeRequest.addressDetail();
        this.postalCode = influencerChangeRequest.postalCode();
        this.influencer.change(influencerChangeRequest);
    }

    public void change(String profileImage) {
        this.profileImage = profileImage;
    }

    // 완성
    public EnterpriserResponse toOwnerResponse() {
        return new EnterpriserResponse(this.profileImage, this.nickname);
    }

    // 완료
    public EnterpriserChangeResponse toOwnerChangeResponse() {

        return new EnterpriserChangeResponse(
                this.email,
                this.name,
                this.phone,
                this.address,
                this.addressDetail,
                this.postalCode);
    }

    // 완료
    public InfluencerResponse toInfluencerResponse() {

        // 사용자가 좋아요한 캠페인 수 구하기
        int likeCnt = this.campaignList.size();

        // List<Sns> -> List<SnsResponse>
        List<SnsResponse> snsResponseList =
                this.influencer.getSnsList().stream().map(Sns::toSnsResponse).toList();

        // 신청한 캠페인 수 구하기 - 취소는 포함안함
        Long appliedCampaignCount =
                this.applicationList.stream()
                        .filter(application -> !application.getIsDeleted())
                        .count();

        // 선정된 캠페인 수
        Long selectedCampaignCount =
                this.applicationList.stream()
                        .filter(application -> application.getState() == State.APPROVED)
                        .count();

        // 진행중인 캠페인 수
        Long ongoingCampaignCount =
                this.applicationList.stream()
                        .filter(
                                application ->
                                        LocalDateTime.now()
                                                        .isAfter(
                                                                application
                                                                        .getCampaign()
                                                                        .getExperienceStartDate())
                                                && LocalDateTime.now()
                                                        .isBefore(
                                                                application
                                                                        .getCampaign()
                                                                        .getExperienceEndDate()))
                        .count();

        // 취소한 캠페인 수
        Long cancelledCampaignCount =
                this.applicationList.stream().filter(Application::getIsDeleted).count();

        return new InfluencerResponse(
                this.nickname,
                this.profileImage,
                snsResponseList,
                likeCnt,
                appliedCampaignCount,
                selectedCampaignCount,
                ongoingCampaignCount,
                cancelledCampaignCount);
    }

    // 완료
    public InfluencerChangeResponse toInfluencerChangeResponse() {

        // 사용자가 좋아요한 캠페인 수 구하기
        int likeCnt = this.campaignList.size();

        // List<Sns> -> List<SnsResponse>
        List<SnsResponse> snsResponseList =
                this.influencer.getSnsList().stream().map(Sns::toSnsResponse).toList();

        return new InfluencerChangeResponse(
                this.name,
                this.profileImage,
                snsResponseList,
                likeCnt,
                this.email,
                this.phone,
                this.nickname,
                this.address,
                this.addressDetail,
                this.postalCode,
                this.influencer.getBirthday(),
                this.influencer.getGender());
    }
}

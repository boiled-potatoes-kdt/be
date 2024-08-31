package com.dain_review.domain.user.model.entity;


import com.dain_review.domain.application.model.entity.Application;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.post.model.entity.Post;
import com.dain_review.domain.user.exception.UserErrorCode;
import com.dain_review.domain.user.exception.UserException;
import com.dain_review.domain.user.model.entity.enums.Role;
import com.dain_review.domain.user.model.request.EnterpriserChangeRequest;
import com.dain_review.domain.user.model.request.EnterpriserExtraRegisterRequest;
import com.dain_review.domain.user.model.request.InfluencerChangeRequest;
import com.dain_review.domain.user.model.request.InfluencerExtraRegisterRequest;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
    private String password;
    private String nickname;

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

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Post> posts;

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

    public void delete() {
        this.isDeleted = true;
    }

    public void change(EnterpriserChangeRequest enterpriserChangeRequest) {
        if (!this.password.equals(enterpriserChangeRequest.oldPassword())) {
            throw new UserException(UserErrorCode.FAILED_CHANGE);
        }
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
        if (!this.password.equals(influencerChangeRequest.oldPassword())) {
            throw new UserException(UserErrorCode.FAILED_CHANGE);
        }
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

    public boolean isNotSame(Long userId) {
        return !this.getId().equals(userId);
    }
}

package com.dain_review.domain.user.model.entity;


import com.dain_review.domain.application.model.entity.Application;
import com.dain_review.domain.post.model.entity.Post;
import com.dain_review.domain.user.model.entity.enums.Role;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "user")
public class User extends BaseEntity {

    private String name;
    private String email;
    private String password;
    private String nickname;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Application> applicationList;

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
    private Boolean isDeleted;
    private Boolean penalty;
}

package com.intouchDTM.hospitalityMinistry.User;

import com.intouchDTM.hospitalityMinistry.OAuth2.OAuth2UserInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "user_table")
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "phone number", unique = true)
    private String phoneNumber;

    @Column(name = "gender")
    private MemberGender memberGender;

    @Column(unique = true)
    private String oauth2Id;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Enumerated(EnumType.STRING)
    private MemberRole role;



    public Member update(OAuth2UserInfo oAuth2UserInfo) {
        this.email = oAuth2UserInfo.getEmail();
        this.name = oAuth2UserInfo.getName();
        this.oauth2Id = oAuth2UserInfo.getOAuth2Id();
        return this;
    }
}

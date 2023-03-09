package com.dangol.dangolsonnimbackend.customer.domain;

import com.dangol.dangolsonnimbackend.customer.dto.CustomerSignupDTO;
import com.dangol.dangolsonnimbackend.type.RoleType;
import com.dangol.dangolsonnimbackend.type.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_customer")
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    private String profileImageUrl;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String birth;

    @Column(nullable = false)
    private Boolean marketingAgreement;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(nullable = false, unique = true)
    private String socialId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    public Customer(CustomerSignupDTO dto) {
        this.email = getEmail();
        this.name = dto.getName();
        this.nickname = dto.getNickname();
        this.profileImageUrl = dto.getProfileImageUrl();
        this.phoneNumber = dto.getPhoneNumber();
        this.birth = dto.getBirth();
        this.marketingAgreement = dto.getMarketingAgreement();
        this.roleType = RoleType.USER;

    }

    public void authorizeCustomer() {
        this.roleType = RoleType.USER;
    }
}

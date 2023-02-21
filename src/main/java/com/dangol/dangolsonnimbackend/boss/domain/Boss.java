package com.dangol.dangolsonnimbackend.boss.domain;

import com.dangol.dangolsonnimbackend.boss.dto.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.dto.BossUpdateRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_boss")
public class Boss {

    @Id
    @Column
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean marketingAgreement;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    public Boss(BossSignupRequestDTO dto) {
        this.name = dto.getName();
        this.password = dto.getPassword();
        this.phoneNumber = dto.getPhoneNumber();
        this.email = dto.getEmail();
        this.marketingAgreement = dto.getMarketingAgreement();
    }

    public void updateInfo(BossUpdateRequestDTO dto) {
        this.phoneNumber = dto.getPhoneNumber() != null ? dto.getPhoneNumber() : this.phoneNumber;
        this.marketingAgreement = dto.getMarketingAgreement() != null ? dto.getMarketingAgreement() : this.marketingAgreement;
    }

    public void updatePassword(String encodedPassword){
        this.password = encodedPassword;
    }
}

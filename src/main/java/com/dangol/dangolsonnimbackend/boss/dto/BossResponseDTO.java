package com.dangol.dangolsonnimbackend.boss.dto;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BossResponseDTO {
    private String name;
    private String phoneNumber;
    private String email;
    private Boolean marketingAgreement;
    private String createAt;

    public BossResponseDTO(Boss boss){
        this.name = boss.getName();
        this.phoneNumber = boss.getPhoneNumber();
        this.email = boss.getEmail();
        this.marketingAgreement = boss.getMarketingAgreement();
        this.createAt = String.valueOf(boss.getCreatedAt());
    }
}

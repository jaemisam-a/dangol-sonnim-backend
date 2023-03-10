package com.dangol.dangolsonnimbackend.boss.dto;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BossResponseDTO {
    private String name;
    private String phoneNumber;
    private String email;
    private Boolean marketingAgreement;
    private String createdAt;

    public BossResponseDTO(Boss boss){
        this.name = boss.getName();
        this.phoneNumber = boss.getPhoneNumber();
        this.email = boss.getEmail();
        this.marketingAgreement = boss.getMarketingAgreement();
        this.createdAt = String.valueOf(boss.getCreatedAt());
    }
}

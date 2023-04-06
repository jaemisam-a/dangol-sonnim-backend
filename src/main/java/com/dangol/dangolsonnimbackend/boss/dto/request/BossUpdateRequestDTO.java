package com.dangol.dangolsonnimbackend.boss.dto.request;

import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BossUpdateRequestDTO {

    @Size(min = 11, max = 11, message = "휴대폰 번호는 11자 이여야 합니다.")
    private String phoneNumber;

    private Boolean marketingAgreement;
    private String accountHolder;
    private String account;
    private String bank;
}

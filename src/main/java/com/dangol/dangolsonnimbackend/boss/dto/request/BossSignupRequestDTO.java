package com.dangol.dangolsonnimbackend.boss.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class BossSignupRequestDTO {

    @NotNull(message = "이름은 Null 일 수 없습니다.")
    @Size(min = 1, max = 8, message = "이름은 1 ~ 8자 이여야 합니다.")
    private String name;

    @NotNull(message = "휴대폰 번호는 Null 일 수 없습니다.")
    @Size(min = 11, max = 11, message = "휴대폰 번호는 11자 이여야 합니다.")
    private String phoneNumber;

    @NotNull(message = "이메일은 Null 일 수 없습니다.")
    @Email
    private String email;

    @NotNull
    @Size(min = 8, message = "비밀번호는 8자 이상이여야 합니다.")
    private String password;

    @NotNull(message = "마케팅 수신 정보 동의는 Null 일 수 없습니다.")
    private Boolean marketingAgreement;

    public void passwordEncode(String encodedPassword){
        this.password = encodedPassword;
    }
}

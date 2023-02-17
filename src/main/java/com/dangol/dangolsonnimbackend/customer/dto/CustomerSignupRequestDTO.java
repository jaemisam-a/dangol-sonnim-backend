package com.dangol.dangolsonnimbackend.customer.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CustomerSignupRequestDTO {

    @NotNull(message = "이름은 Null 일 수 없습니다.")
    @Size(min = 1, max = 8, message = "이름은 1 ~ 8자 사이여야 합니다.")
    private String name;

    @NotNull(message = "이메일은 Null 일 수 없습니다.")
    @Email
    private String email;

    @NotNull(message = "가입 유형은 Null 일 수 없습니다.")
    private String providerType;

    @NotNull(message = "닉네임은 Null 일 수 없습니다.")
    @Size(min = 2, max = 12, message = "닉네임은 2 ~ 12자 사이여야 합니다.")
    private String nickname;

    private String profileImageUrl;

    @NotNull(message = "휴대폰 번호는 Null 일 수 없습니다.")
    @Size(min = 11, max = 11, message = "휴대폰 번호는 11자 이여야 합니다.")
    private String phoneNumber;

    @NotNull(message = "생년월일은 Null 일 수 없습니다.")
    @Size(min = 8, max = 8, message = "생년월일은 8자 이여야 합니다.")
    private String birth;
}

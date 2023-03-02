package com.dangol.dangolsonnimbackend.boss.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class BossSigninReqeustDTO {

    @NotNull(message = "이메일은 Null 일 수 없습니다.")
    @Email
    private String email;

    @NotNull
    @Size(min = 8, message = "비밀번호는 8자 이상이여야 합니다.")
    private String password;
}

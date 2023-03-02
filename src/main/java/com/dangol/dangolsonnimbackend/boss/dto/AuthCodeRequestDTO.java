package com.dangol.dangolsonnimbackend.boss.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthCodeRequestDTO {
    @NotNull(message = "이메일은 Null 일 수 없습니다.")
    @Email
    private String email;
}

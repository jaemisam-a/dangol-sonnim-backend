package com.dangol.dangolsonnimbackend.boss.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class BossPasswordUpdateReqeuestDTO {
    @NotNull
    @Size(min = 8, message = "비밀번호는 8자 이상이여야 합니다.")
    private String password;
}

package com.dangol.dangolsonnimbackend.boss.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BossFindEmailReqeustDTO {
    @NotNull(message = "휴대폰 번호는 Null 일 수 없습니다.")
    @Size(min = 11, max = 11, message = "휴대폰 번호는 11자 이여야 합니다.")
    private String phoneNumber;
}
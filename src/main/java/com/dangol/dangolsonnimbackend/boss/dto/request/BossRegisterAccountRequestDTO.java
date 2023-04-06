package com.dangol.dangolsonnimbackend.boss.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BossRegisterAccountRequestDTO {
    @NotNull(message = "예금주는 Null 일 수 없습니다.")
    private String accountHolder;
    @NotNull(message = "계좌는 Null 일 수 없습니다.")
    private String account;
    @NotNull(message = "은행은 Null 일 수 없습니다.")
    private String bank;
}

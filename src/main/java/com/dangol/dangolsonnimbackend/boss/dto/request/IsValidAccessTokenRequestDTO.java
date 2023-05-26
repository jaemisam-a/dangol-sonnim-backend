package com.dangol.dangolsonnimbackend.boss.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IsValidAccessTokenRequestDTO {
    @NotNull
    private String accessToken;
}

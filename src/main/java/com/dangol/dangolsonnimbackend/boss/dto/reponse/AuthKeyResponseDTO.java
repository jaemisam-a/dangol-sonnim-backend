package com.dangol.dangolsonnimbackend.boss.dto.reponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthKeyResponseDTO {
    private String authKey;

    public AuthKeyResponseDTO(String authKey){
        this.authKey = authKey;
    }
}

package com.dangol.dangolsonnimbackend.boss.dto.reponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthKeyResponseDTO {
    private Boolean isValid;

    public AuthKeyResponseDTO(Boolean isValid){
        this.isValid = isValid;
    }
}

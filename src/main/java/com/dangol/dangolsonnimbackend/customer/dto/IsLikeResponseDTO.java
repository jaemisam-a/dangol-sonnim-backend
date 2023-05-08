package com.dangol.dangolsonnimbackend.customer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IsLikeResponseDTO {
    private Boolean isLike;

    public IsLikeResponseDTO(Boolean isLike){
        this.isLike = isLike;
    }
}

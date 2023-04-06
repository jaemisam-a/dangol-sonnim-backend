package com.dangol.dangolsonnimbackend.subscribe.dto;

import com.dangol.dangolsonnimbackend.subscribe.domain.Benefit;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize
public class BenefitDTO {
    private String description;

    public BenefitDTO(Benefit benefit){
        this.description = benefit.getDescription();
    }
}
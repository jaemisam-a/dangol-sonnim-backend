package com.dangol.dangolsonnimbackend.boss.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BossFindEmailResponseDTO extends RepresentationModel<BossFindEmailResponseDTO> {
    private String email;
}
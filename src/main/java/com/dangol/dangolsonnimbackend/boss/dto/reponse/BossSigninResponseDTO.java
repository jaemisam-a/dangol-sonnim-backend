package com.dangol.dangolsonnimbackend.boss.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BossSigninResponseDTO extends RepresentationModel<BossSigninResponseDTO> {
    private String accessToken;
}

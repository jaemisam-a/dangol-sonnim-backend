package com.dangol.dangolsonnimbackend.boss.service;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.BossSigninReqeustDTO;
import com.dangol.dangolsonnimbackend.boss.dto.BossSigninResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.BossSignupRequestDTO;

public interface BossService {

    void signup(BossSignupRequestDTO dto);
    Boss findByEmail(String email);

    BossSigninResponseDTO getByCredentials(BossSigninReqeustDTO reqeustDTO);
}

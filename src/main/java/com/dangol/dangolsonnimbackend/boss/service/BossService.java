package com.dangol.dangolsonnimbackend.boss.service;

import com.dangol.dangolsonnimbackend.boss.dto.BossSigninReqeustDTO;
import com.dangol.dangolsonnimbackend.boss.dto.BossSigninResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.BossSignupRequestDTO;

public interface BossService {

    void signup(BossSignupRequestDTO dto);
    BossSigninResponseDTO getByCredentials(BossSigninReqeustDTO dto);
}

package com.dangol.dangolsonnimbackend.boss.service;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.BossSigninReqeustDTO;
import com.dangol.dangolsonnimbackend.boss.dto.BossSigninResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.dto.BossUpdateRequestDTO;

public interface BossService {

    void signup(BossSignupRequestDTO dto);
    void withdraw(String email);
    Boss findByEmail(String email);
    Boss update(String email, BossUpdateRequestDTO dto);
    BossSigninResponseDTO getByCredentials(BossSigninReqeustDTO reqeustDTO);
}

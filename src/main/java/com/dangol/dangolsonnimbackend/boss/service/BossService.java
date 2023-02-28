package com.dangol.dangolsonnimbackend.boss.service;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.*;

public interface BossService {

    void signup(BossSignupRequestDTO dto);
    void withdraw(String email);
    Boss findByEmail(String email);
    Boss update(String email, BossUpdateRequestDTO dto);
    void updatePassword(BossPasswordUpdateReqeuestDTO dto);
    BossSigninResponseDTO getByCredentials(BossSigninReqeustDTO reqeustDTO);
    BossFindEmailResponseDTO findEmailByPhoneNumber(BossFindEmailReqeustDTO reqeustDTO);
}

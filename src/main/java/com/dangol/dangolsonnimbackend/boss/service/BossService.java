package com.dangol.dangolsonnimbackend.boss.service;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.reponse.BossFindEmailResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.reponse.BossResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.reponse.BossSigninResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.request.*;

public interface BossService {

    BossResponseDTO signup(BossSignupRequestDTO dto);
    void withdraw(String email);
    Boss findByEmail(String email);
    BossResponseDTO update(String email, BossUpdateRequestDTO dto);
    BossResponseDTO updatePassword(BossPasswordUpdateReqeuestDTO dto);
    BossSigninResponseDTO getByCredentials(BossSigninReqeustDTO reqeustDTO);
    BossFindEmailResponseDTO findEmailByPhoneNumber(BossFindEmailReqeustDTO reqeustDTO);
}

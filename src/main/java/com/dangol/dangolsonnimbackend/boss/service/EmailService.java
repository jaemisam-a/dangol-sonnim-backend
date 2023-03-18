package com.dangol.dangolsonnimbackend.boss.service;

import com.dangol.dangolsonnimbackend.boss.dto.reponse.AuthKeyResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.request.AuthKeyRequestDTO;
import com.dangol.dangolsonnimbackend.boss.dto.request.IsValidAuthCodeRequestDTO;

public interface EmailService {
    void sendEmail(String toEmail, String authCode);
    void create(String email, String authCode);
    String generateAuthCode();
    AuthKeyResponseDTO isValidAuthCode(IsValidAuthCodeRequestDTO dto);
}

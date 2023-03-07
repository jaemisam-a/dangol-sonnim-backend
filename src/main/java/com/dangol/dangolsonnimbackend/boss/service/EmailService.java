package com.dangol.dangolsonnimbackend.boss.service;


public interface EmailService {
    void sendEmail(String toEmail, String authCode);
    String generateAuthCode();
}

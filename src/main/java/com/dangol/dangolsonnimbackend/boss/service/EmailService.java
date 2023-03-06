package com.dangol.dangolsonnimbackend.boss.service;

import java.util.concurrent.CompletableFuture;

public interface EmailService {
    void sendEmail(String toEmail, String AuthCode);
    String generateAuthCode();
}

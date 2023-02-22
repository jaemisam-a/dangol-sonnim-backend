package com.dangol.dangolsonnimbackend.boss.controller;

import com.dangol.dangolsonnimbackend.boss.dto.AuthCodeRequestDTO;
import com.dangol.dangolsonnimbackend.boss.dto.AuthCodeResponseDTO;
import com.dangol.dangolsonnimbackend.boss.service.EmailService;
import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {
    private final Map<String, Integer> requestCountMap = new HashMap<>();
    private static final int MAX_REQUEST_COUNT = 5;
    private static final int DEFAULT_REQUEST_COUNT = 0;
    private static final int INCREASE_REQUEST_COUNT = 1;
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-auth-code")
    public ResponseEntity<?> sendAuthCode(@Valid @RequestBody AuthCodeRequestDTO dto) {
        int requestCount = requestCountMap.getOrDefault(dto.getEmail(), DEFAULT_REQUEST_COUNT);

        if (requestCount >= MAX_REQUEST_COUNT) {
            throw new BadRequestException(ErrorCodeMessage.DAILY_EMAIL_RESTRICTED);
        }

        requestCountMap.put(dto.getEmail(), requestCount + INCREASE_REQUEST_COUNT);
        AuthCodeResponseDTO responseDTO = new AuthCodeResponseDTO(emailService.sendEmail(dto.getEmail()));
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Scheduled(cron = "0 0 6 * * *")
    public void resetRequestCount() {
        requestCountMap.clear();
    }
}

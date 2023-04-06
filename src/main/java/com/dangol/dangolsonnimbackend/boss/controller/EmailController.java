package com.dangol.dangolsonnimbackend.boss.controller;

import com.dangol.dangolsonnimbackend.boss.dto.reponse.AuthKeyResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.request.AuthKeyRequestDTO;
import com.dangol.dangolsonnimbackend.boss.dto.request.IsValidAuthCodeRequestDTO;
import com.dangol.dangolsonnimbackend.boss.service.EmailService;
import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/email")
@Slf4j
public class EmailController {
    private final Map<String, Integer> requestCountMap = new ConcurrentHashMap<>();
    // TODO 클라이언트 테스트 완료시 MAX_REQUEST_COUNT 요청 제한 수정
    private static final int MAX_REQUEST_COUNT = 100;
    private static final int DEFAULT_REQUEST_COUNT = 0;
    private static final int INCREASE_REQUEST_COUNT = 1;
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-auth-code")
    public ResponseEntity<AuthKeyResponseDTO> sendAuthCode(@Valid @RequestBody AuthKeyRequestDTO dto) {
        int requestCount = requestCountMap.getOrDefault(dto.getEmail(), DEFAULT_REQUEST_COUNT);

        if (requestCount >= MAX_REQUEST_COUNT) {
            throw new BadRequestException(ErrorCodeMessage.DAILY_EMAIL_RESTRICTED);
        }

        requestCountMap.put(dto.getEmail(), requestCount + INCREASE_REQUEST_COUNT);

        // 이메일 서비스
        String authCode = emailService.generateAuthCode();
        emailService.sendEmail(dto.getEmail(), authCode);
        emailService.create(dto.getEmail(), authCode);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/valid")
    public ResponseEntity<AuthKeyResponseDTO> isValidAuthCode(@Valid @RequestBody IsValidAuthCodeRequestDTO requestDTO){
        AuthKeyResponseDTO responseDTO = emailService.isValidAuthCode(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Scheduled(cron = "0 0 6 * * *")
    public void resetRequestCount() {
        requestCountMap.clear();
    }
}

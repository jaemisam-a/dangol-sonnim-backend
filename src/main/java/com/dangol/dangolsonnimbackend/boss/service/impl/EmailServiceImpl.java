package com.dangol.dangolsonnimbackend.boss.service.impl;

import com.dangol.dangolsonnimbackend.boss.domain.Email;
import com.dangol.dangolsonnimbackend.boss.dto.reponse.AuthKeyResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.request.AuthKeyRequestDTO;
import com.dangol.dangolsonnimbackend.boss.dto.request.IsValidAuthCodeRequestDTO;
import com.dangol.dangolsonnimbackend.boss.repository.EmailRepository;
import com.dangol.dangolsonnimbackend.boss.repository.dsl.EmailQueryRepository;
import com.dangol.dangolsonnimbackend.boss.service.EmailService;
import com.dangol.dangolsonnimbackend.errors.InternalServerException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    private final EmailRepository emailRepository;
    private final EmailQueryRepository emailQueryRepository;
    private static final String CHAR_POOL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int INDEX_EIGHT = 8;

    public EmailServiceImpl(JavaMailSender emailSender, TemplateEngine templateEngine, EmailRepository emailRepository, EmailQueryRepository emailQueryRepository) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.emailRepository = emailRepository;
        this.emailQueryRepository = emailQueryRepository;
    }

    @Async
    public void sendEmail(String toEmail, String authCode) {
        sendEmailWithTemplate(toEmail, authCode);
    }

    @Transactional
    public void create(String email, String authCode){
        Email found = emailQueryRepository.findByEmail(email)
                .orElseGet(() -> emailRepository.save(new Email(email, authCode)));

        found.setAuthCode(authCode);
    }

    // 랜덤 인증코드 생성
    public String generateAuthCode() {
        return RandomStringUtils.random(INDEX_EIGHT, CHAR_POOL);
    }

    @Override
    public AuthKeyResponseDTO isValidAuthCode(IsValidAuthCodeRequestDTO dto) {
        return new AuthKeyResponseDTO(emailQueryRepository.existsByEmailAndAuthCode(dto.getEmail(), dto.getAuthCode()));
    }

    private void sendEmailWithTemplate(String toEmail, String authCode) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            // 템플릿에서 사용될 변수를 저장
            Context context = new Context();
            context.setVariable("authCode", authCode);
            String content = templateEngine.process("mail", context);

            // 이메일 설정
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("jaemisama12@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("단골손님 인증번호");
            helper.setText(content, true);

            emailSender.send(message);
        } catch (MessagingException e) {
            throw new InternalServerException(ErrorCodeMessage.EMAIL_SEND_FAILURE);
        } catch (RuntimeException e) {
            throw new InternalServerException(ErrorCodeMessage.RESPONSE_CREATE_ERROR);
        }
    }
}

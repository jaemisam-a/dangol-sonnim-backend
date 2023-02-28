package com.dangol.dangolsonnimbackend.boss.service.impl;

import com.dangol.dangolsonnimbackend.boss.service.EmailService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    private static final String CHAR_POOL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int INDEX_EIGHT = 8;

    public EmailServiceImpl(JavaMailSender emailSender, TemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String toEmail, String authCode) {
        sendEmailWithTemplate(toEmail, authCode);
    }

    // 랜덤 인증코드 생성
    public String generateAuthCode() {
        return RandomStringUtils.random(INDEX_EIGHT, CHAR_POOL);
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
            throw new RuntimeException("이메일 전송에 실패하였습니다.", e);
        } catch (RuntimeException e) {
            throw new RuntimeException("다른 원인으로 인해 이메일 전송에 실패하였습니다.", e);
        }
    }
}

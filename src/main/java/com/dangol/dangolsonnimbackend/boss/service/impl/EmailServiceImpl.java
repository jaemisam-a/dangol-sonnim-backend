package com.dangol.dangolsonnimbackend.boss.service.impl;

import com.dangol.dangolsonnimbackend.boss.service.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
    private static final int INDEX_ZERO = 0;
    private static final int INDEX_EIGHT = 8;

    public EmailServiceImpl(JavaMailSender emailSender, TemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    public String sendEmail(String toEmail) {
        String authCode = generateAuthCode();
        sendEmailWithTemplate(toEmail, authCode);
        return authCode;
    }

    // 랜덤 인증코드 생성
    private String generateAuthCode() {
        Random random = new Random();
        StringBuilder authCode = new StringBuilder();
        for (int i = INDEX_ZERO; i < INDEX_EIGHT; i++) {
            int index = random.nextInt(CHAR_POOL.length());
            authCode.append(CHAR_POOL.charAt(index));
        }
        return authCode.toString();
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
        }
    }
}

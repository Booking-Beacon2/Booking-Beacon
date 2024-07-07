package com.bteam.Booking_Beacon.global.util;

import com.bteam.Booking_Beacon.global.exception.UnHandledUserException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
    @Autowired private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}") private String serviceName;

    public void sendEmail(String to, String subject, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
            helper.setFrom(serviceName); // 발신자
            helper.setTo(to); // 수신자
            helper.setSubject(subject); // 이메일 제목
            helper.setText(content,true); // 이메일 본문
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new UnHandledUserException(e.getMessage());
        }
    }
}

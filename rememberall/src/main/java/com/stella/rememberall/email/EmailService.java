package com.stella.rememberall.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableAsync
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    @Async
    @Transactional
    public Boolean testEmail() {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo("minpearl0826@gmail.com");
        smm.setSubject("Rememberall 회원가입 이메일 인증");
        smm.setText("테스트여");
        javaMailSender.send(smm);
        return true;
    }
}

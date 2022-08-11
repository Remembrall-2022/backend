package com.stella.rememberall.user.emailAuth;

import com.stella.rememberall.user.exception.MemberException;
import com.stella.rememberall.user.exception.MyErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.mail.internet.MimeMessage;

@Component
@EnableAsync
@RequiredArgsConstructor
public class EmailUtil {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.mail.password}")
    private String fromEmailpassword;

    private final JavaMailSender emailSender;

    @Async
    @Transactional
    public void sendEmail(String data, String email) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            message.addRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("Rememberall 회원가입 이메일 인증");
            message.setContent(createSignUpEmailContent(data),"text/html;charset=euc-kr");
            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MemberException(MyErrorCode.EMAIL_SEND_FAIL);
        }
    }

    private String createSignUpEmailContent(String data) {
        return String.format(
            "<h1>[이메일 인증]</h1> <p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p><br><form action=\"http://localhost:8080/signup/confirm?key=%s\" method=\"POST\" enctype=\"text/plain\" name=\"EmailForm\">" +
                    "<button type=\"submit\" value=\"이메일 인증하기\" style=\"background-color: turquoise; border: none; border-radius: 5px; color: #ffffff;; padding: 15px 32px\">이메일 인증하기</button></form>"
                , data
        );
    }
}

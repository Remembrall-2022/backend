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

    @Value("${cloud.ec2.server}")
    private String ec2ServerAddress;

    private final JavaMailSender emailSender;

    @Async
    @Transactional
    public void sendSignUpEmail(String data, String email) {
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
            "<h1>[이메일 인증]</h1> <p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p><br><form action=\""+ec2ServerAddress+"\" method=\"POST\" enctype=\"text/plain\" name=\"EmailForm\">" +
                    "<button type=\"submit\" value=\"이메일 인증하기\" style=\"background-color: turquoise; border: none; border-radius: 5px; color: #ffffff;; padding: 15px 32px\">이메일 인증하기</button></form>"
                , data
        );
    }

    @Async
    @Transactional
    public void sendAuthCodeEmail(String authCode, String email) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            message.addRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("Rememberall 비밀번호 변경을 위한 인증 번호");
            message.setContent(createAuthCodeEmailContent(authCode),"text/html;charset=euc-kr");
            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MemberException(MyErrorCode.EMAIL_SEND_FAIL);
        }
    }

    private String createAuthCodeEmailContent(String authCode) {
        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 rememberall 이메일 인증 칸에 입력하세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += authCode;
        msg += "</td></tr></tbody></table></div>";
        return msg;
    }
}

package com.stella.rememberall.email;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EmailController {
    private final EmailService emailService;

    @GetMapping("/test-email")
    public void confirmEmail() {
        emailService.testEmail();
    }
}

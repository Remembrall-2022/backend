package com.stella.rememberall.user.emailAuth;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableAsync
@RequiredArgsConstructor
public class EmailAuthService {
    private final EmailUtil emailUtil;

    @Async
    @Transactional
    public void sendSignUpAuthEmail(String redisKey, String email) {
        emailUtil.sendSignUpEmail(redisKey, email);
    }

    @Async
    @Transactional
    public void sendAuthCodeEmail(String authCode, String email){
        emailUtil.sendAuthCodeEmail(authCode, email);
    }

}

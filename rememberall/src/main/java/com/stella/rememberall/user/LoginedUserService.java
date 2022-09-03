package com.stella.rememberall.user;

import com.stella.rememberall.security.SecurityUtil;
import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.user.exception.MemberException;
import com.stella.rememberall.user.exception.MyErrorCode;
import com.stella.rememberall.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginedUserService {
    private final UserRepository userRepository;
    public User getLoginedUser(){
        return SecurityUtil.getCurrentUserPk().flatMap(userRepository::findById)
                .orElseThrow(() -> new MemberException(MyErrorCode.USER_NOT_FOUND));
    }
}

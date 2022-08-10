package com.stella.rememberall.user;

import com.stella.rememberall.security.dto.TokenDto;
import com.stella.rememberall.security.dto.TokenRequestDto;
import com.stella.rememberall.user.dto.EmailUserLoginRequestDto;
import com.stella.rememberall.user.dto.EmailUserSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/signup/email")
    public Long join(@RequestBody EmailUserSaveRequestDto dto) {
        return userService.saveEmailUser(dto);
    }

    @GetMapping("/login/email")
    public TokenDto login(@RequestBody EmailUserLoginRequestDto requestDto){
        return userService.login(requestDto);
    }

    @PostMapping("/reissue")
    public TokenDto reissue(@RequestBody TokenRequestDto dto, HttpServletRequest httpServletRequest){
        return userService.reissue(dto, httpServletRequest);
    }
}

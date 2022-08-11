package com.stella.rememberall.user;

import com.stella.rememberall.common.response.OnlyResponseString;
import com.stella.rememberall.user.emailAuth.dto.EmailSendResponseDto;
import com.stella.rememberall.security.dto.TokenDto;
import com.stella.rememberall.security.dto.TokenRequestDto;
import com.stella.rememberall.user.dto.EmailUserLoginRequestDto;
import com.stella.rememberall.user.dto.EmailUserSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/signup/email")
    public EmailSendResponseDto join(@RequestBody @Valid EmailUserSaveRequestDto dto) {
        userService.validateSignUpWithEmail(dto);
        return EmailSendResponseDto.of(dto.getEmail(), "이메일 전송에 성공했습니다.");
    }

    @GetMapping("/login/email")
    public TokenDto login(@RequestBody @Valid EmailUserLoginRequestDto requestDto){
        return userService.login(requestDto);
    }

    @PostMapping("/reissue")
    public TokenDto reissue(@RequestBody TokenRequestDto dto, HttpServletRequest httpServletRequest){
        return userService.reissue(dto, httpServletRequest);
    }

    @PostMapping("/signup/confirm")
    public OnlyResponseString signupConfirm(@RequestParam("key") String key, HttpServletResponse response) {
        userService.registerUser(key);
        return new OnlyResponseString("성공!");
    }
}

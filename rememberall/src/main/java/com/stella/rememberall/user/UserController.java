package com.stella.rememberall.user;

import com.stella.rememberall.common.response.OnlyResponseString;
import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.user.dto.*;
import com.stella.rememberall.user.emailAuth.dto.EmailSendResponseDto;
import com.stella.rememberall.security.dto.TokenDto;
import com.stella.rememberall.security.dto.TokenRequestDto;
import com.stella.rememberall.user.emailAuth.dto.EmailUserAuthRequestDto;
import com.stella.rememberall.user.emailAuth.dto.EmailUserPasswordUpdateRequestDto;
import com.stella.rememberall.user.emailAuth.dto.EmailUserValidRequestDto;
import com.stella.rememberall.user.exception.MemberException;
import com.stella.rememberall.user.exception.MyErrorCode;
import com.stella.rememberall.user.kakao.KakaoProfile;
import com.stella.rememberall.user.kakao.KakaoUserService;
import com.stella.rememberall.user.kakao.KakaoSignService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final EmailUserService emailUserService;
    private final KakaoUserService kakaoUserService;
    private final KakaoSignService kakaoSignService;
    private final UserService userService;

    @PostMapping("/signup/email/authcode/send")
    public EmailSendResponseDto sendEmail(@RequestBody @Valid EmailUserAuthRequestDto requestDto) {
        emailUserService.sendAuthCode(requestDto);
        return EmailSendResponseDto.of(requestDto.getEmail(), "이메일 전송에 성공했습니다.");
    }

    @PostMapping("/signup/email/authcode")
    public OnlyResponseString authEmail(@RequestBody @Valid EmailUserValidRequestDto requestDto){
        return emailUserService.validEmail(requestDto);
    }

    @PostMapping("/signup/email")
    public OnlyResponseString join(@RequestBody @Valid EmailUserSaveRequestDto dto) {
        emailUserService.registerUser(dto);
        return new OnlyResponseString("회원가입에 성공했습니다.");
    }

//    redis 버전
//    @PostMapping("/signup/email")
//    public EmailSendResponseDto join(@RequestBody @Valid EmailUserSaveRequestDto dto) {
//        emailUserService.validateSignUpWithEmail(dto);
//        return EmailSendResponseDto.of(dto.getEmail(), "이메일 전송에 성공했습니다.");
//    }

    @PostMapping("/signup/kakao")
    public TokenDto join(@RequestBody Map<String, String> kakaoTokenMap) {
        KakaoProfile kakaoProfile = kakaoUserService.getKakaoProfile(kakaoTokenMap.get("kakaoToken"));
        return kakaoSignService.socialSignup(kakaoProfile, kakaoTokenMap.get("kakaoToken"));
    }

    @PostMapping("/login/email")
    public TokenDto login(@RequestBody @Valid EmailUserLoginRequestDto requestDto){
        return emailUserService.login(requestDto);
    }

    @PostMapping("/login/kakao")
    public TokenDto login(@RequestBody Map<String, String> kakaoTokenMap){
        KakaoProfile kakaoProfile = kakaoUserService.getKakaoProfile(kakaoTokenMap.get("kakaoToken"));
        return kakaoSignService.kakaoLogin(kakaoProfile, kakaoTokenMap.get("kakaoToken"));
    }

    @PostMapping("/reissue")
    public TokenDto reissue(@RequestBody TokenRequestDto dto){
        return userService.reissue(dto);
    }

//    @PostMapping("/signup/confirm")
//    public OnlyResponseString signupConfirm(@RequestParam("key") String key, HttpServletResponse response) {
//        emailUserService.registerUser(key);
//        return new OnlyResponseString("성공!");
//    }

    @GetMapping("/user/info")
    public UserInfoResponseDto getUserInfo(@AuthenticationPrincipal User user){
        return userService.getUserInfo(user);
    }

    @PostMapping("/user/name")
    public OnlyResponseString updateUsername(@RequestBody @Valid NameUpdateRequestDto newName, @AuthenticationPrincipal User user){
        return userService.updateMyName(newName.getName(), user);
    }

    @PostMapping("/user/alarm/agree")
    public OnlyResponseString updateAlarmAgree(@RequestBody @Valid AgreeUpdateRequestDto newAlarmAgree, @AuthenticationPrincipal User user){
        return userService.updateAlarmAgree(newAlarmAgree.getAgree(), user);
    }

    @PostMapping("/user/term/agree")
    public OnlyResponseString updateTermAgree(@RequestBody @Valid AgreeUpdateRequestDto newTermAgree, @AuthenticationPrincipal User user){
        return userService.updateTermAgree(newTermAgree.getAgree(), user);
    }

    @PostMapping("/user/password/request")
    public EmailSendResponseDto requestEmailValidationForPasswordUpdate(@RequestBody @Valid EmailUserAuthRequestDto requestDto){
        emailUserService.requestEmailValidation(requestDto);
        return EmailSendResponseDto.of(requestDto.getEmail(), "이메일 전송에 성공했습니다.");
    }

    @PostMapping("/user/password/valid")
    public OnlyResponseString validEmailWithAuthCode(@RequestBody @Valid EmailUserValidRequestDto requestDto){
        return emailUserService.validEmail(requestDto);
    }

    @PostMapping("/user/password")
    public OnlyResponseString updatePassword(@RequestBody @Valid EmailUserPasswordUpdateRequestDto requestDto){
        return emailUserService.updatePasswordIfEmailAuthed(requestDto);
    }

    @DeleteMapping("/user/signout")
    public OnlyResponseString deleteUser(@AuthenticationPrincipal User user){
        userService.signout(user);
        return new OnlyResponseString("회원 탈퇴에 성공했습니다.");
    }

}

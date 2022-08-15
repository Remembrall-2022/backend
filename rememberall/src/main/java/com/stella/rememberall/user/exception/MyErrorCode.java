package com.stella.rememberall.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MyErrorCode {

    INVALID_REQUEST("잘못된 요청입니다"),
    DUPLICATED_EMAIL("이미 이메일로 가입한 회원입니다."),
    DUPLICATED_KAKAO("이미 카카오로 가입한 회원입니다."),
    USER_NOT_FOUND("요청한 회원이 존재하지 않습니다."),
    USER_NOT_FOUND_FROM_REDIS("회원 가입을 요청한 회원이 아닙니다."),
    WRONG_PASSWORD("비밀번호가 틀렸습니다."),
    EMAIL_SEND_FAIL("이메일 전송에 실패했습니다."),
    TIMEOUT_AUTH_REQUEST("인증 유효 시간이 지났습니다.");

    private String defaultErrorMessage;

}

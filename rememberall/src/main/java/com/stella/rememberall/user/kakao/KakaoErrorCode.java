package com.stella.rememberall.user.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KakaoErrorCode {
    INVALID_KAKAO_TOKEN("유효하지 않은 카카오 토큰입니다."),
    INVALID_KAKAO_USER("조회한 카카오 프로필의 이메일이 null입니다.");

    private String defaultErrorMessage;
}

package com.stella.rememberall.user.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KakaoErrorCode {
    COMMUNICATE_FAIL("카카오 서버에 연결할 수 없습니다."),
    INVALID_TOKEN("유효하지 않은 카카오 토큰입니다.");

    private String defaultErrorMessage;
}

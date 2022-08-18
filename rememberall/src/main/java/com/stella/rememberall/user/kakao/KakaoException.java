package com.stella.rememberall.user.kakao;

import lombok.Getter;

@Getter
public class KakaoException extends RuntimeException{
    private KakaoErrorCode errorCode;
    private String errorMessage;

    public KakaoException(KakaoErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultErrorMessage();
    }
    public KakaoException(KakaoErrorCode errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

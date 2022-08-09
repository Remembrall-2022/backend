package com.stella.rememberall.user.exception;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {
    private MyErrorCode errorCode;
    private String errorMessage;

    public MemberException(MyErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultErrorMessage();
    }
    public MemberException(MyErrorCode errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

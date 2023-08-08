package com.stella.rememberall.common.exception.jpa;

import lombok.Getter;

@Getter
public class CommonJpaException extends RuntimeException{
    private CommonJpaErrorCode errorCode;
    private String errorMessage;

    public CommonJpaException(CommonJpaErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultErrorMessage();
    }
    public CommonJpaException(CommonJpaErrorCode errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

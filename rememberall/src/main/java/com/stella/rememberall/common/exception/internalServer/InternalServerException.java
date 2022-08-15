package com.stella.rememberall.common.exception.internalServer;

import lombok.Getter;

@Getter
public class InternalServerException extends RuntimeException {
    private InternalServerErrorCode errorCode;
    private String errorMessage;

    public InternalServerException(InternalServerErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultErrorMessage();
    }
    public InternalServerException(InternalServerErrorCode errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

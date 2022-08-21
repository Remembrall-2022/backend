package com.stella.rememberall.userLogImg.exception;

import lombok.Getter;

@Getter
public class EmptyFileException extends RuntimeException{
    private FileErrorCode errorCode;
    private String errorMessage;

    public EmptyFileException(FileErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultErrorMessage();
    }
    public EmptyFileException(FileErrorCode errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

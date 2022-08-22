package com.stella.rememberall.placelog.exception;

import lombok.Getter;

@Getter
public class PlaceLogException extends RuntimeException{
    private PlaceLogErrorCode errorCode;
    private String errorMessage;

    public PlaceLogException(PlaceLogErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultErrorMessage();
    }
    public PlaceLogException(PlaceLogErrorCode errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

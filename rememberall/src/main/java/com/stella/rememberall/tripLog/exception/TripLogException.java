package com.stella.rememberall.tripLog.exception;

import lombok.Getter;

@Getter
public class TripLogException extends RuntimeException {
    private TripLogErrorCode errorCode;
    private String errorMessage;

    public TripLogException(TripLogErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultErrorMessage();
    }
    public TripLogException(TripLogErrorCode errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

package com.stella.rememberall.datelog.exception;

import lombok.Getter;

@Getter
public class DateLogException extends RuntimeException{

    private DateLogExCode exCode;
    private String errorMessage;

    public DateLogException(DateLogExCode code) {
        this.exCode = code;
        this.errorMessage = code.getDefaultMessage();
    }

    public DateLogException(DateLogExCode code, String errorMessage) {
        this.exCode = code;
        this.errorMessage = errorMessage;
    }
}

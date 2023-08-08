package com.stella.rememberall.tripLog.exception;

import lombok.Getter;

@Getter
public class NotBlankException extends RuntimeException {
    private String msg;
    public NotBlankException(String msg) {
        this.msg = msg;
    }
    public String getErrorName(){
        return "NotBlank";
    }
}

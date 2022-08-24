package com.stella.rememberall.dongdong;

import lombok.Getter;

@Getter
public class DongdongException extends RuntimeException{

    private DongdongExCode exCode;
    private String errorMessage;

    public DongdongException(DongdongExCode code) {
        this.exCode = code;
        this.errorMessage = code.getDefaultMessage();
    }

    public DongdongException(DongdongExCode code, String errorMessage) {
        this.exCode = code;
        this.errorMessage = errorMessage;
    }
}

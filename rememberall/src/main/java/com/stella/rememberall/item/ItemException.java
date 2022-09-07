package com.stella.rememberall.item;

import lombok.Getter;

@Getter
public class ItemException extends RuntimeException{

    private ItemErrorCode errorCode;
    private String errorMessage;

    public ItemException(ItemErrorCode errorCode){
        this.errorCode = errorCode;
    }

    public ItemException(ItemErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

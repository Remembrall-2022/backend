package com.stella.rememberall.datelog.exception;

import com.stella.rememberall.datelog.domain.Question;
import lombok.Getter;

@Getter
public class QuestionException extends RuntimeException{

    private QuestionExCode exCode;
    private String errorMessage;

    public QuestionException(QuestionExCode code) {
        this.exCode = code;
        this.errorMessage = code.getDefaultMessage();
    }

    public QuestionException(QuestionExCode code, String errorMessage) {
        this.exCode = code;
        this.errorMessage = errorMessage;
    }
}

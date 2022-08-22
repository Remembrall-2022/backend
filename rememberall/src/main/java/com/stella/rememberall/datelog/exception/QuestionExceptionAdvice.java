package com.stella.rememberall.datelog.exception;

import com.stella.rememberall.common.response.ErrorEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class QuestionExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(QuestionException.class)
    public ErrorEntity questionException(QuestionException e) {
        log.error("[QuestionException] {} - {}", e.getExCode(), e.getErrorMessage());
        return new ErrorEntity(e.getExCode().toString(), e.getErrorMessage());
    }
}

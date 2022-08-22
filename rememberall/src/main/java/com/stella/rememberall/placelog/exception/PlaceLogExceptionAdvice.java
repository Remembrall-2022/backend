package com.stella.rememberall.placelog.exception;

import com.stella.rememberall.common.response.ErrorEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class PlaceLogExceptionAdvice {
    @ExceptionHandler(PlaceLogException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorEntity authException(PlaceLogException e) {
        log.error("PlaceLog Exception({}) - {}", e.getErrorCode().toString(), e.getErrorMessage());
        return new ErrorEntity(e.getErrorCode().toString(), e.getErrorMessage());
    }
}

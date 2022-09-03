package com.stella.rememberall.tripLog.exception;

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
public class TripLogExceptionAdvice {
    @ExceptionHandler(TripLogException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorEntity tripLogException(TripLogException e) {
        log.error("TripLog Exception({}) - {}", e.getErrorCode(), e.getErrorMessage());
        return new ErrorEntity(e.getErrorCode().toString(), e.getErrorMessage()); // TODO : e.getResponseCode()로 개선 필요
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorEntity notBlankException(NotBlankException e) {
        log.error("Not Blank Exception({}) - {}", e.getErrorName(), e.getMsg());
        return new ErrorEntity(e.getErrorName(), e.getMsg()); // TODO : e.getResponseCode()로 개선 필요
    }
}

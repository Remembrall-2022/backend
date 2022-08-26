package com.stella.rememberall.dongdong;

import com.stella.rememberall.common.response.ErrorEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class DongdongExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DongdongException.class)
    public ErrorEntity dongdongException(DongdongException e) {
        log.error("DongdongException[{}] - {}", e.getExCode(), e.getErrorMessage());
        return new ErrorEntity(e.getExCode().toString(), e.getErrorMessage());
    }

}

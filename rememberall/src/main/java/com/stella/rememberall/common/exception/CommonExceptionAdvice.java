package com.stella.rememberall.common.exception;

import com.stella.rememberall.common.exception.jpa.CommonJpaException;
import com.stella.rememberall.common.response.ErrorEntity;
import com.stella.rememberall.user.exception.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class CommonExceptionAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorEntity handleValidationExceptions(MethodArgumentNotValidException e) {
        String errorCode = e.getBindingResult().getAllErrors().get(0).getCode();
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error("MethodArgumentNotValid Exception({}) - {}", errorCode, errorMessage);
        return new ErrorEntity(errorCode, errorMessage); // TODO : e.getResponseCode()로 개선 필요
    }

    @ExceptionHandler(CommonJpaException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorEntity commonJpaException(CommonJpaException e) {
        log.error("CommonJpa Exception({}) - {}", e.getErrorCode(), e.getErrorMessage());
        return new ErrorEntity(e.getErrorCode().toString(), e.getErrorMessage()); // TODO : e.getResponseCode()로 개선 필요
    }
}

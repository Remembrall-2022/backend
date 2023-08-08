package com.stella.rememberall.user.exception;

import com.stella.rememberall.common.response.ErrorEntity;
import com.stella.rememberall.user.kakao.KakaoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class MemberExceptionAdvice {
    @ExceptionHandler(MemberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorEntity authenticationEntrypointException(MemberException e) {
        log.error("Member Exception({}) - {}", e.getErrorCode(), e.getErrorMessage());
        return new ErrorEntity(e.getErrorCode().toString(), e.getErrorMessage()); // TODO : e.getResponseCode()로 개선 필요
    }

    @ExceptionHandler(KakaoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorEntity authenticationEntrypointException(KakaoException e) {
        log.error("Member Exception({}) - {}", e.getErrorCode(), e.getErrorMessage());
        return new ErrorEntity(e.getErrorCode().toString(), e.getErrorMessage()); // TODO : e.getResponseCode()로 개선 필요
    }
}

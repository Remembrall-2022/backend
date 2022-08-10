package com.stella.rememberall.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TripLogErrorCode {
    TRIPLOG_NOT_FOUND("요청한 일기장을 찾을 수 없습니다.");

    private String defaultErrorMessage;
}

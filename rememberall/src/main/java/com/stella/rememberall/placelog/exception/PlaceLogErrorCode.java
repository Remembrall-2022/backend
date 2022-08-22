package com.stella.rememberall.placelog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlaceLogErrorCode {
    NOT_FOUND("존재하지 않는 관광지 정보입니다.");

    private String defaultErrorMessage;
}

package com.stella.rememberall.datelog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DateLogExCode {

    DUPLICATED_DATELOG("이미 같은 일기가 존재합니다."),
    DATELOG_NOT_FOUND("해당 일기를 찾을 수 없습니다.");

    private String defaultMessage;
}

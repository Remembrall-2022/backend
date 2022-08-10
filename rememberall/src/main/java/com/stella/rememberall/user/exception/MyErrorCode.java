package com.stella.rememberall.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MyErrorCode {

    INVALID_REQUEST("잘못된 요청입니다"),
    DUPLICATED_REQUEST("기존 요청과 중복되어 처리할 수 없습니다."),
    INTERNAL_SERVER_ERROR("처리 중 에러가 발생했습니다."),
    ENTITY_NOT_FOUND("리소스가 존재하지 않습니다."),
    WRONG_PASSWORD("비밀번호가 틀렸습니다.");

    private String defaultErrorMessage;

}

package com.stella.rememberall.datelog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionExCode {

    QUESTION_NOT_FOUND("해당 id의 질문을 찾을 수 없습니다."),
    QUESTION_NOT_EXISTED("저장된 질문이 없습니다.");

    private String defaultMessage;
}

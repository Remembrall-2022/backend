package com.stella.rememberall.common.exception.jpa;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonJpaErrorCode {
    SAVE_FAIL("엔티티 저장에 실패했습니다.");

    private String defaultErrorMessage;
}

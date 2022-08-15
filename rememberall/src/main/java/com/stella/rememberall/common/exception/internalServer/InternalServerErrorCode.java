package com.stella.rememberall.common.exception.internalServer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InternalServerErrorCode {
    COMMUNICATE_FAIL("통신에 실패했습니다.");

    private String defaultErrorMessage;
}

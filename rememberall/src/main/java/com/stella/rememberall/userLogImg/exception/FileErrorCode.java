package com.stella.rememberall.userLogImg.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileErrorCode {
    EMPTY_FILE("파일이 없습니다. 파일을 첨부해주세요."),
    FILE_UPLOAD_FAIL("파일 업로드에 실패했습니다.");
    private String defaultErrorMessage;
}

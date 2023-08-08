package com.stella.rememberall.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorEntity {
    private String errorName;
    private String errorMessage;
}

package com.stella.rememberall.datelog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QnAUpdateRequestDto {
    private Long questionId;
    private String answer;
}

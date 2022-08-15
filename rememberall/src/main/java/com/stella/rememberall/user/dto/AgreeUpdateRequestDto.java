package com.stella.rememberall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AgreeUpdateRequestDto {
    @NotNull(message = "새로운 설정 정보는 빈값일 수 없습니다.")
    private Boolean agree;
}

package com.stella.rememberall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NameUpdateRequestDto {
    @NotEmpty(message = "새로운 이름은 빈값일 수 없습니다.")
    private String name;
}

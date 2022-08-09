package com.stella.rememberall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class EmailUserLoginRequestDto {
    private String email;
    private String password;
}

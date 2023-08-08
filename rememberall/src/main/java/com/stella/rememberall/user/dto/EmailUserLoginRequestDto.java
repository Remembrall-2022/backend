package com.stella.rememberall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class EmailUserLoginRequestDto {
    @Email private String email;
    @NotEmpty(message = "회원의 password는 빈값일 수 없습니다.") private String password;
}

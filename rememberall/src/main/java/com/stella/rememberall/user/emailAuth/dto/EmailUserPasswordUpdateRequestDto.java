package com.stella.rememberall.user.emailAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class EmailUserPasswordUpdateRequestDto {
    @Email private String email;
    @NotEmpty(message = "비밀번호는 빈 값일 수 없습니다.") private String newPassword;
}

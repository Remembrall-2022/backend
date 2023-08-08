package com.stella.rememberall.user.emailAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class EmailUserAuthRequestDto {
    @Email private String email;
}

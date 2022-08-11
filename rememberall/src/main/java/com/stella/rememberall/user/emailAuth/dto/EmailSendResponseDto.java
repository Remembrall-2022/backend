package com.stella.rememberall.user.emailAuth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailSendResponseDto {
    private String email;
    private String message;

    public static EmailSendResponseDto of(String email, String message){
        EmailSendResponseDto emailSendResponseDto = new EmailSendResponseDto(email, message);
        return emailSendResponseDto;
    }

    public EmailSendResponseDto(String email, String message){
        this.email = email;
        this.message = message;
    }
}

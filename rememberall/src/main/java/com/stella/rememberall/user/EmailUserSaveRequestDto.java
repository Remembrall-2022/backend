package com.stella.rememberall.user;

import com.stella.rememberall.domain.AuthType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmailUserSaveRequestDto {
    String email;
    String password;
    String name;

    public User toEntity(){
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .authType(AuthType.EMAIL)
                .build();
    }


}

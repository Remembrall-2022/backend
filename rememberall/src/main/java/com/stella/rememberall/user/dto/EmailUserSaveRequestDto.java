package com.stella.rememberall.user.dto;

import com.stella.rememberall.domain.AuthType;
import com.stella.rememberall.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Getter
@AllArgsConstructor
public class EmailUserSaveRequestDto {
    String email;
    String password;
    String name;

    public User toEntityWithEncodedPassword(PasswordEncoder passwordEncoder){
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .authType(AuthType.EMAIL)
                .password(passwordEncoder.encode(password))
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }

    public User toEntity(){
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .authType(AuthType.EMAIL)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }


}

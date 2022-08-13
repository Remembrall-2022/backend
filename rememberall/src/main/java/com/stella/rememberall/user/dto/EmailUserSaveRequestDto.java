package com.stella.rememberall.user.dto;

import com.stella.rememberall.domain.AuthType;
import com.stella.rememberall.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Collections;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailUserSaveRequestDto {
    @Email String email;
    @NotEmpty(message = "회원의 password는 빈값일 수 없습니다.") String password;
    @NotEmpty(message = "회원의 name은 빈값일 수 없습니다.") String name;

    public User toEntityWithEncodedPassword(PasswordEncoder passwordEncoder){
        return User.builder()
                .uniqueValue(email)
                .password(password)
                .name(name)
                .authType(AuthType.EMAIL)
                .password(passwordEncoder.encode(password))
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }

    public User toEntity(){
        return User.builder()
                .uniqueValue(email)
                .password(password)
                .name(name)
                .authType(AuthType.EMAIL)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }


}

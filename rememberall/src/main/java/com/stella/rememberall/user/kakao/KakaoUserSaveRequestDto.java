package com.stella.rememberall.user.kakao;

import com.stella.rememberall.domain.AuthType;
import com.stella.rememberall.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoUserSaveRequestDto {

    String email;
    Long kakaoId;
    String name;

    public User toEntity() {
        return User.builder()
                .email(email)
                .kakaoId(kakaoId)
                .name(name)
                .authType(AuthType.KAKAO)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }
}

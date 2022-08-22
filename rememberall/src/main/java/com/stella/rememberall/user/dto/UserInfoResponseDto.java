package com.stella.rememberall.user.dto;

import com.stella.rememberall.domain.AuthType;
import com.stella.rememberall.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponseDto {
    private String email;
    private String name;
    private AuthType authType;
    private Boolean alarmAgree;
    private Boolean termAgree;

    public static UserInfoResponseDto of(User user){
        return UserInfoResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .authType(user.getAuthType())
                .alarmAgree(user.getAlarmAgree())
                .termAgree(user.getTermAgree())
                .build();
    }
}

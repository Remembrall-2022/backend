package com.stella.rememberall.user.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserAttendRewardDto {

    @NotNull
    private Long userId;
}

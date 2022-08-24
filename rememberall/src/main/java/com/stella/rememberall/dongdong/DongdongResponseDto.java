package com.stella.rememberall.dongdong;

import com.stella.rememberall.user.domain.User;
import lombok.Builder;

public class DongdongResponseDto {

    private User user;
    private Long point;
    private Long exp;
    private DongdongImg dongdongImg;

    private Integer level;

    @Builder
    public DongdongResponseDto(User user, Long point, Long exp, DongdongImg dongdongImg, Integer level) {
        this.user = user;
        this.point = point;
        this.exp = exp;
        this.dongdongImg = dongdongImg;
        this.level = level;
    }
}

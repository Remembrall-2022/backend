package com.stella.rememberall.dongdong;

import com.stella.rememberall.user.domain.User;
import lombok.Builder;

public class DongdongResponseDto {

    private User user;
    private Long point;
    private Long exp;
    private DongdongImg dongdongImg;

    private Integer lv;

    @Builder
    public DongdongResponseDto(User user, Long point, Long exp, DongdongImg dongdongImg, Integer lv) {
        this.user = user;
        this.point = point;
        this.exp = exp;
        this.dongdongImg = dongdongImg;
        this.lv = lv;
    }
}

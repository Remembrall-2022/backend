package com.stella.rememberall.dongdong;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DongdongResponseDto {

    private Long userId;
    private Long point;
    private Long exp;
    private DongdongImg dongdongImg;

    private Integer level;

    @Builder
    public DongdongResponseDto(Long userId, Long point, Long exp, DongdongImg dongdongImg, Integer level) {
        this.userId = userId;
        this.point = point;
        this.exp = exp;
        this.dongdongImg = dongdongImg;
        this.level = level;
    }
}

package com.stella.rememberall.dongdong;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DongdongResponseDto {

    private Long userId;
    private Long point;
    private Long exp;
    private Long maxExp;
    private String dongdongImgUrl;

    private Integer level;

    @Builder
    public DongdongResponseDto(Long userId, Long point, Long exp, Long maxExp, String dongdongImgUrl, Integer level) {
        this.userId = userId;
        this.point = point;
        this.exp = exp;
        this.maxExp = maxExp;
        this.dongdongImgUrl = dongdongImgUrl;
        this.level = level;
    }
}

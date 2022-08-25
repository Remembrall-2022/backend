package com.stella.rememberall.dongdong;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DongdongReward {
    ATTENDANCE(50L, 100L),
    DATELOG(100L, 200L),
    DATELOG_S(0L, 200L),
    SHARE(200L, 0L);

    private Long point;
    private Long exp;
}

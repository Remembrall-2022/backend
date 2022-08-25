package com.stella.rememberall.dongdong;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
public class DongdongLevelRule {

    private Integer level;
    private Long minExp;
    private DongdongImg dongdongImg;


}

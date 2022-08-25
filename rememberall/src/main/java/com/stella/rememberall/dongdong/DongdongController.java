package com.stella.rememberall.dongdong;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DongdongController {

    private final DongdongService dongdongService;

    @GetMapping("/dongdong/{userId}")
    public DongdongResponseDto readDongdong(@PathVariable Long userId) {
        return dongdongService.readDongdong(userId);
    }

    @GetMapping("/attendReward/{userId}")
    public DongdongResponseDto requestReward(@PathVariable Long userId) {return dongdongService.reward(userId, DongdongReward.ATTENDANCE); }
}

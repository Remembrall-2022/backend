package com.stella.rememberall.dongdong;

import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.user.dto.UserAttendRewardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class DongdongController {

    private final DongdongService dongdongService;

    @GetMapping("/dongdong")
    public DongdongResponseDto readDongdong(@AuthenticationPrincipal User user) {
        return dongdongService.readDongdong(user);
    }

    @PostMapping("/dongdong/attendance")
    public DongdongResponseDto requestReward(@AuthenticationPrincipal User user) {
        return dongdongService.reward(DongdongReward.ATTENDANCE);
    }
}

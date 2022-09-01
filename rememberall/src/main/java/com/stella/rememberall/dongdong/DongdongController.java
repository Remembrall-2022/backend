package com.stella.rememberall.dongdong;

import com.stella.rememberall.user.dto.UserAttendRewardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class DongdongController {

    private final DongdongService dongdongService;

    @GetMapping("/dongdong")
    public DongdongResponseDto readDongdong() {
        return dongdongService.readDongdong();
    }

    @PostMapping("/dongdong/attendance")
    public DongdongResponseDto requestReward(@RequestBody @Valid UserAttendRewardDto dto) {
        return dongdongService.reward(dto.getUserId(), DongdongReward.ATTENDANCE);
    }
}

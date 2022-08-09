package com.stella.rememberall.tripLog;

import com.stella.rememberall.domain.AuthType;
import com.stella.rememberall.tripLog.dto.TripLogResponseDto;
import com.stella.rememberall.tripLog.dto.TripLogSaveRequestDto;
import com.stella.rememberall.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TripLogController {

    private final TripLogService tripLogService;

    @PostMapping("triplog/new")
    public TripLogResponseDto create(@RequestBody TripLogSaveRequestDto requestDto){
        return tripLogService.saveTripLog(requestDto);
    }

    @GetMapping("triplog/{id}")
    public TripLogResponseDto findOne(@PathVariable Long id){
        return tripLogService.findTripLog(id);
    }

    @GetMapping("triplog")
    public List<TripLogResponseDto> findTripLogList(){
        User user = new User(AuthType.EMAIL, "email", "pw", "name");
        return tripLogService.findTripLogList(user);
    }

}

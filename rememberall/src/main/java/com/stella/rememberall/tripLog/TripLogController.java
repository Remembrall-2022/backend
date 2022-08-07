package com.stella.rememberall.tripLog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TripLogController {

    private final TripLogService tripLogService;

    @PostMapping("triplog/new")
    public boolean create(@RequestBody TripLogSaveRequestDto requestDto){
        System.out.println("requestDto.getTitle() = " + requestDto.getTitle());
        tripLogService.saveTripLog(requestDto);
        return true;
    }
}

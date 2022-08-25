package com.stella.rememberall.tripLog;

import com.stella.rememberall.tripLog.dto.TripLogResponseDto;
import com.stella.rememberall.tripLog.dto.TripLogSaveRequestDto;
import com.stella.rememberall.tripLog.dto.TripLogUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class TripLogController {

    private final TripLogService tripLogService;

    @PostMapping("/tripLog/new")
    public TripLogResponseDto create(@RequestBody @Valid TripLogSaveRequestDto requestDto){
        return tripLogService.saveTripLog(requestDto);
    }

    @GetMapping("/tripLog/{id}")
    public TripLogResponseDto findOne(@PathVariable Long id){
        return tripLogService.findTripLog(id);
    }

    @GetMapping("/tripLog/list")
    public List<TripLogResponseDto> findTripLogList(){
        return tripLogService.findTripLogList();
    }

    @PostMapping("/tripLog/{id}")
    public TripLogResponseDto update(@RequestBody @Valid TripLogUpdateRequestDto requestDto, @PathVariable Long id){
        return tripLogService.updateTripLog(requestDto, id);
    }


}

package com.stella.rememberall.tripLog;

import com.stella.rememberall.common.response.OnlyResponseString;
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
    public Long create(@RequestBody @Valid TripLogSaveRequestDto requestDto){
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
    public Long update(@RequestBody @Valid TripLogUpdateRequestDto requestDto, @PathVariable Long id){
        return tripLogService.updateTripLog(requestDto, id);
    }

    @DeleteMapping("/tripLog/{id}")
    public OnlyResponseString deleteTripLog(@PathVariable Long id){
        tripLogService.deleteTripLog(id);
        return new OnlyResponseString("일기장 삭제에 성공했습니다.");
    }


}

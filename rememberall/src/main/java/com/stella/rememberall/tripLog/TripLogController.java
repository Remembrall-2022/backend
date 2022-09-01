package com.stella.rememberall.tripLog;

import com.stella.rememberall.common.response.OnlyResponseString;
import com.stella.rememberall.tripLog.dto.*;
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

    @GetMapping("/tripLog/{id}/onlyId")
    public TripLogWithPlaceLogIdListResponseDto findOneOnlyId(@PathVariable Long id){
        return tripLogService.findTripLogWithPlaceLogIdList(id);
    }

    @GetMapping("/tripLog/{id}")
    public TripLogWithWholePlaceLogListResponseDto findOneWholeData(@PathVariable Long id){
        return tripLogService.findTripLogWithWholePlaceLogList(id);
    }

    @GetMapping("/tripLog/list")
    public List<TripLogSimpleResponseDto> findTripLogList(){
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

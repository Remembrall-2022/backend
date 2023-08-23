package com.stella.rememberall.tripLog;

import com.stella.rememberall.common.response.OnlyResponseString;
import com.stella.rememberall.tripLog.dto.*;
import com.stella.rememberall.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class TripLogController {

    private final TripLogService tripLogService;

    @PostMapping("/tripLog/new")
    public OnlyResponseString create(@RequestBody @Valid TripLogSaveRequestDto requestDto, @AuthenticationPrincipal User user){
        return new OnlyResponseString("일기장을 생성했습니다. id: " + tripLogService.saveTripLog(requestDto, user));
    }

    @GetMapping("/tripLog/{id}/onlyId")
    public TripLogWithPlaceLogIdListResponseDto findOneOnlyId(@PathVariable Long id, @AuthenticationPrincipal User user){
        return tripLogService.findTripLogWithPlaceLogIdList(id, user);
    }

    @GetMapping("/tripLog/{id}")
    public TripLogWithWholePlaceLogListResponseDto findOneWholeData(@PathVariable Long id, @AuthenticationPrincipal User user){
        return tripLogService.findTripLogWithWholePlaceLogList(id, user);
    }

    @GetMapping("/tripLog/list")
    public List<TripLogSimpleResponseDto> findTripLogList(@AuthenticationPrincipal User user){
        return tripLogService.findTripLogList(user);
    }

    @PostMapping("/tripLog/{id}")
    public OnlyResponseString update(@RequestBody @Valid TripLogUpdateRequestDto requestDto, @PathVariable Long id, @AuthenticationPrincipal User user){
        return new OnlyResponseString("일기장을 수정했습니다. id: " + tripLogService.updateTripLog(requestDto, id, user));
    }

    @DeleteMapping("/tripLog/{id}")
    public OnlyResponseString deleteTripLog(@PathVariable Long id, @AuthenticationPrincipal User user){
        tripLogService.deleteTripLog(id, user);
        return new OnlyResponseString("일기장 삭제에 성공했습니다.");
    }


}

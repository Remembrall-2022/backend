package com.stella.rememberall.datelog;

import com.stella.rememberall.common.response.OnlyResponseString;
import com.stella.rememberall.datelog.dto.*;
import com.stella.rememberall.placelog.SpotResponseDto;
import com.stella.rememberall.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DateLogController {

    private final DateLogService dateLogService;

    @PostMapping("/tripLog/{tripLogId}/dateLog/new")
    public OnlyResponseString createDateLog(
            @PathVariable("tripLogId") Long tripLogId,
            @RequestPart @Valid DateLogSaveRequestDto saveRequestDto,
            @RequestPart(value = "file") List<MultipartFile> multipartFiles,
            @AuthenticationPrincipal User user) {
        Long dateLogId = dateLogService.createDateLog(tripLogId, saveRequestDto, multipartFiles, user);
        return new OnlyResponseString("일기를 생성했습니다. id: " + dateLogId);
    }

    @PostMapping("/dateLog/{dateLogId}/date")
    public OnlyResponseString updateDate(@PathVariable Long dateLogId, @RequestBody @Valid DateUpdateRequestDto date){

        dateLogService.updateDate(dateLogId, date);
        return new OnlyResponseString("날짜별 일기의 날짜를 수정했습니다.");
    }

    @PostMapping("/dateLog/{dateLogId}/question-answer")
    public OnlyResponseString updateQnA(@PathVariable Long dateLogId, @RequestBody QnAUpdateRequestDto qnA){
        dateLogService.updateQnA(dateLogId, qnA);
        return new OnlyResponseString("날짜별 일기의 질문과 답변을 수정했습니다.");
    }

    @PostMapping("/dateLog/{dateLogId}/weather")
    public OnlyResponseString updateQnA(@PathVariable Long dateLogId, @RequestBody WeatherInfoUpdateRequestDto weatherInfo){
        dateLogService.updateWeatherInfo(dateLogId, weatherInfo);
        return new OnlyResponseString("날짜별 일기의 날씨 정보를 수정했습니다.");
    }

    @PostMapping("/dateLog/{dateLogId}/placeLog/index")
    public OnlyResponseString updatePlaceLogIndex(@PathVariable Long dateLogId, @RequestBody @Valid PlaceLogIndexUpdateRequestDto indexInfo){
        dateLogService.updatePlaceLogIndex(dateLogId, indexInfo);
        return new OnlyResponseString("날짜별 일기의 관광지별 일기 인덱스를 수정했습니다.");
    }

    @PostMapping("/dateLog/{dateLogId}")
    public OnlyResponseString updatePlaceLog(@PathVariable Long dateLogId, @RequestBody @Valid DateLogUpdateRequestDto updateRequestDto){
        dateLogService.updateDateLog(dateLogId, updateRequestDto);
        return new OnlyResponseString("날짜별 일기와 그에 속한 관광지별 일기 정보를 수정했습니다.");
    }

    @GetMapping("/tripLog/{tripLogId}/dateLog/{dateLogId}")
    public DateLogResponseDto readDateLog(@PathVariable Long tripLogId, @PathVariable Long dateLogId, @AuthenticationPrincipal User user) {
        return dateLogService.readDateLogFromTripLog(dateLogId, tripLogId, user);
    }

    @GetMapping("/dateLog/today")
    public DateLogResponseDto readTodayDateLog(@AuthenticationPrincipal User user) {
        return dateLogService.readTodayDateLog(user);
    }

    @GetMapping("/tripLog/{tripLogId}/{date}")
    public DateExistResponseDto checkDateDuplicateInTripLog(
            @PathVariable Long tripLogId,
            @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        return new DateExistResponseDto(dateLogService.checkDateDuplicateInTripLog(tripLogId, date));
    }


    //별자리 지도 api -> 지워도 됨

    @GetMapping("/tripLog/{tripLogId}/dateLog/{dateLogId}/spots")
    public List<SpotResponseDto> readSpotListFromDateLog(@PathVariable Long tripLogId, @PathVariable Long dateLogId, @AuthenticationPrincipal User user) {
        return dateLogService.getSpotListFromDateLog(tripLogId, dateLogId, user);
    }

    @GetMapping("/tripLog/{tripLogId}/spots")
    public List<SpotResponseDto> readSpotListFromTripLog(@PathVariable Long tripLogId, @AuthenticationPrincipal User user) {
        return dateLogService.getSpotListFromTripLog(tripLogId, user);
    }

    @GetMapping("/tripLog/{tripLogId}/spots/distinct")
    public List<SpotResponseDto> readDistinctSpotListFromTripLog(@PathVariable Long tripLogId, @AuthenticationPrincipal User user) {
        return dateLogService.getDistinctSpotListFromTripLog(tripLogId, user);
    }


    //별자리 지도 api 끝

    @DeleteMapping("/tripLog/{tripLogId}/dateLog/{dateLogId}")
    public OnlyResponseString deleteDateLog(@PathVariable Long tripLogId, @PathVariable Long dateLogId, @AuthenticationPrincipal User user) {
        dateLogService.deleteDateLog(dateLogId, tripLogId, user);
        return new OnlyResponseString("날짜별 일기를 삭제했습니다.");
    }

}

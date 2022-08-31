package com.stella.rememberall.datelog;

import com.stella.rememberall.common.response.OnlyResponseString;
import com.stella.rememberall.datelog.dto.*;
import com.stella.rememberall.datelog.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DateLogController {

    private final DateLogService dateLogService;
    private final QuestionRepository questionRepository;

    @PostMapping("/tripLog/{tripLogId}/dateLog/new")
    public Long createDateLog(
            @PathVariable("tripLogId") Long tripLogId,
            @RequestPart @Valid DateLogSaveRequestDto saveRequestDto,
            @RequestPart(value = "file") List<MultipartFile> multipartFiles
            ) {
        return dateLogService.createDateLog(tripLogId, saveRequestDto, multipartFiles);
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

    @GetMapping("/tripLog/{tripLogId}/dateLog/{dateLogId}")
    public DateLogResponseDto readDateLog(@PathVariable Long tripLogId, @PathVariable Long dateLogId) {
        return dateLogService.readDateLogFromTripLog(dateLogId, tripLogId);
    }

    @DeleteMapping("/tripLog/{tripLogId}/dateLog/{dateLogId}")
    public OnlyResponseString deleteDateLog(@PathVariable Long tripLogId, @PathVariable Long dateLogId) {
        dateLogService.deleteDateLog(dateLogId, tripLogId);
        return new OnlyResponseString("날짜별 일기를 삭제했습니다.");
    }

}

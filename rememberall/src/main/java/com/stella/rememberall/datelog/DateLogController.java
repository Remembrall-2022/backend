package com.stella.rememberall.datelog;

import com.stella.rememberall.common.response.OnlyResponseString;
import com.stella.rememberall.datelog.dto.DateLogResponseDto;
import com.stella.rememberall.datelog.dto.DateLogSaveRequestDto;
import com.stella.rememberall.datelog.repository.QuestionRepository;
import com.stella.rememberall.placelog.SpotResponseDto;
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

    // 관광지도 같이 저장
    @PostMapping("/tripLog/{id}/dateLog/new")
    public Long createDateLog(
            @PathVariable("id") Long tripLogId,
            @RequestPart @Valid DateLogSaveRequestDto saveRequestDto,
            @RequestPart(value = "file") List<MultipartFile> multipartFiles
            ) {
        return dateLogService.createDateLog(tripLogId, saveRequestDto, multipartFiles);
    }

    @GetMapping("/tripLog/{tripLogId}/dateLog/{dateLogId}")
    public DateLogResponseDto readDateLog(@PathVariable Long tripLogId, @PathVariable Long dateLogId) {
        return dateLogService.readDateLogFromTripLog(dateLogId, tripLogId);
    }

    @GetMapping("/tripLog/{tripLogId}/dateLog/{dateLogId}")
    public List<SpotResponseDto> readSpotList(@PathVariable Long tripLogId, @PathVariable Long dateLogId) {
        return dateLogService.getSpotList(tripLogId, dateLogId);
    }

    @DeleteMapping("/tripLog/{tripLogId}/dateLog/{dateLogId}")
    public OnlyResponseString deleteDateLog(@PathVariable Long tripLogId, @PathVariable Long dateLogId) {
        dateLogService.deleteDateLog(dateLogId, tripLogId);
        return new OnlyResponseString("날짜별 일기를 삭제했습니다.");
    }

}

package com.stella.rememberall.datelog;

import com.stella.rememberall.datelog.dto.DateLogResponseDto;
import com.stella.rememberall.datelog.dto.DateLogSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DateLogController {

    private final DateLogService dateLogService;

    // 관광지도 같이 저장
    @PostMapping("/tripLog/{id}/dateLog/new")
    public Long create(
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

}

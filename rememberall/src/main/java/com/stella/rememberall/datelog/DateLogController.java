package com.stella.rememberall.datelog;

import com.stella.rememberall.datelog.dto.DateLogResponseDto;
import com.stella.rememberall.datelog.dto.DateLogSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class DateLogController {

    private final DateLogService dateLogService;

    //TODO: url에서 triplogid 지우는 방법
    @PostMapping("/triplog/{id}/datelog/new")
    public Long create(@PathVariable("id") Long tripLogId, @RequestBody @Valid DateLogSaveRequestDto saveRequestDto) {
         return dateLogService.createDateLog(tripLogId, saveRequestDto);
    }

    @GetMapping("/datelog/{dateLogId}")
    public DateLogResponseDto readDateLog(@PathVariable Long dateLogId) {
        return dateLogService.readOne(dateLogId);
    }

}

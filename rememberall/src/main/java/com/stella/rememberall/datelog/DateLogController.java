package com.stella.rememberall.datelog;

import com.stella.rememberall.datelog.dto.DateLogSaveRequestDto;
import com.stella.rememberall.tripLog.TripLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/triplog")
public class DateLogController {

    private final DateLogService dateLogService;

    @PostMapping("/{id}/datelog/new")
    public DateLogSaveRequestDto create(@PathVariable("id") Long tripLogId, @RequestBody @Valid DateLogSaveRequestDto saveRequestDto) {
        //파라미터로 받은 일기장 식별자로 일기장 read ->  일기장 찾아서 Dto에 넣기
         return dateLogService.createDateLog(tripLogId, saveRequestDto);
    }

}

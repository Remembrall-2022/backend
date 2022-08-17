package com.stella.rememberall.datelog;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class DateLogController {

    private final DateLogService dateLogService;

    @PostMapping("datelog/new")
    public DateLogSaveRequestDto create(@RequestBody @Valid DateLogSaveRequestDto saveRequestDto) {
         return dateLogService.createDateLog(saveRequestDto);
    }

}

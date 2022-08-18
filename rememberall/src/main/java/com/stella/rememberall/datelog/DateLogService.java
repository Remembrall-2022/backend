package com.stella.rememberall.datelog;

import com.stella.rememberall.datelog.domain.DateLog;
import com.stella.rememberall.datelog.dto.DateLogSaveRequestDto;
import com.stella.rememberall.datelog.exception.DateLogExCode;
import com.stella.rememberall.datelog.exception.DateLogException;
import com.stella.rememberall.datelog.repository.DateLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DateLogService {

    private final DateLogRepository dateLogRepository;

    @Transactional
    public DateLogSaveRequestDto createDateLog(DateLogSaveRequestDto dateLogSaveRequestDto) {

        validateDuplicateDateLog(dateLogSaveRequestDto);
        dateLogRepository.save(dateLogSaveRequestDto.toEntity());
        return dateLogSaveRequestDto;
    }

    private void validateDuplicateDateLog(DateLogSaveRequestDto dateLogSaveRequestDto) {
        List<DateLog> foundDateLog = dateLogRepository.findByTripLogAndDate(dateLogSaveRequestDto.getTripLog(), dateLogSaveRequestDto.getDate());
        if (!foundDateLog.isEmpty()) {
            throw new DateLogException(DateLogExCode.DUPLICATED_DATELOG);
        }
    }
}

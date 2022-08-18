package com.stella.rememberall.datelog;

import com.stella.rememberall.datelog.domain.DateLog;
import com.stella.rememberall.datelog.dto.DateLogSaveRequestDto;
import com.stella.rememberall.datelog.exception.DateLogExCode;
import com.stella.rememberall.datelog.exception.DateLogException;
import com.stella.rememberall.datelog.repository.DateLogRepository;
import com.stella.rememberall.tripLog.TripLog;
import com.stella.rememberall.tripLog.TripLogRepository;
import com.stella.rememberall.tripLog.exception.TripLogException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.stella.rememberall.tripLog.exception.TripLogErrorCode.TRIPLOG_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class DateLogService {

    private final DateLogRepository dateLogRepository;
    private final TripLogRepository tripLogRepository;

    @Transactional
    public DateLogSaveRequestDto createDateLog(Long tripLogId, DateLogSaveRequestDto dateLogSaveRequestDto) {

        //파라미터로 받은 일기장 식별자로 일기장 read ->  일기장 찾아서 Dto에 넣기
        Optional<TripLog> tripLogOptional = tripLogRepository.findById(tripLogId);
        tripLogOptional.orElseThrow(() -> new TripLogException(TRIPLOG_NOT_FOUND, "일기장을 찾을 수 없어 일기를 생성할 수 없습니다."));

        dateLogSaveRequestDto.setTripLog(tripLogOptional.get());
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

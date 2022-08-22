package com.stella.rememberall.datelog;

import com.stella.rememberall.datelog.domain.DateLog;
import com.stella.rememberall.datelog.domain.Question;
import com.stella.rememberall.datelog.domain.WeatherInfo;
import com.stella.rememberall.datelog.dto.DateLogResponseDto;
import com.stella.rememberall.datelog.dto.DateLogSaveRequestDto;
import com.stella.rememberall.datelog.exception.DateLogExCode;
import com.stella.rememberall.datelog.exception.DateLogException;
import com.stella.rememberall.datelog.exception.QuestionExCode;
import com.stella.rememberall.datelog.exception.QuestionException;
import com.stella.rememberall.datelog.repository.DateLogRepository;
import com.stella.rememberall.datelog.repository.QuestionRepository;
import com.stella.rememberall.domain.PlaceLog;
import com.stella.rememberall.tripLog.TripLog;
import com.stella.rememberall.tripLog.TripLogRepository;
import com.stella.rememberall.tripLog.exception.TripLogException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.stella.rememberall.tripLog.exception.TripLogErrorCode.TRIPLOG_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class DateLogService {

    private final DateLogRepository dateLogRepository;
    private final TripLogRepository tripLogRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public Long createDateLog(Long tripLogId, DateLogSaveRequestDto dateLogSaveRequestDto) {
        //파라미터로 받은 일기장 식별자로 일기장 read ->  일기장 찾아서 Dto에 넣기
        Optional<TripLog> tripLogOptional = tripLogRepository.findById(tripLogId);
        tripLogOptional.orElseThrow(() -> new TripLogException(TRIPLOG_NOT_FOUND, "일기장을 찾을 수 없어 일기를 생성할 수 없습니다."));

        //QuestionId 있으면 Question 조회 없으면 null, QuestionId 있는데 질문 찾을 수 없는 경우 예외
        Optional<Long> questionId = Optional.ofNullable(dateLogSaveRequestDto.getQuestionId());
        Question question = null;
        if (questionId.isPresent()) {
            question = questionRepository.findById(questionId.get())
                    .orElseThrow(() -> new QuestionException(QuestionExCode.QUESTION_NOT_FOUND));
        }

        //answer값 있으면 저장, 없으면 null
        String answer = Optional.ofNullable(dateLogSaveRequestDto.getAnswer()).orElse(null);


        //dto 사용해서 datelog 엔티티 생성
        DateLog dateLogEntity = dateLogSaveRequestDto.toEntity().builder()
                .date(dateLogSaveRequestDto.getDate())
                .weatherInfo(dateLogSaveRequestDto.getWeatherInfo())
                .answer(answer)
                .tripLog(tripLogOptional.get())
                .question(question).build();

        // triplog 객체의 datelogList 컬렉션 필드에 새로 생성된 datelog 저장
        tripLogOptional.get().getDateLogList().add(dateLogEntity);
        validateDuplicateDateLog(dateLogEntity);
        dateLogRepository.save(dateLogEntity);

        return dateLogEntity.getId();
    }

    // TODO: 일기 추가하면 경험치, 포인트 주는 로직 개발

    private void validateDuplicateDateLog(DateLog dateLog) {
        List<DateLog> foundDateLog = dateLogRepository.findByTripLogAndDate(dateLog.getTripLog(), dateLog.getDate());
        if (!foundDateLog.isEmpty()) {
            throw new DateLogException(DateLogExCode.DUPLICATED_DATELOG);
        }
    }

    @Transactional
    public DateLogResponseDto readOne(Long dateLogId) {
        DateLog dateLogEntity = dateLogRepository.findById(dateLogId)
                .orElseThrow(() -> new DateLogException(DateLogExCode.DATELOG_NOT_FOUND, "일기를 찾을 수 없어 조회할 수 없습니다."));

        Question question = Optional.ofNullable(dateLogEntity.getQuestion()).orElse(null);
        String answer = Optional.ofNullable(dateLogEntity.getAnswer()).orElse(null);

        //List<PlaceLog> logList


        return DateLogResponseDto.builder()
                .date(dateLogEntity.getDate())
                .weatherInfo(dateLogEntity.getWeatherInfo())
                .question(question)
                .answer(answer)
                .build();
    }

    @Transactional
    public void deleteDateLog(Long dateLogId) {
        dateLogRepository.deleteById(dateLogId);
    }
}

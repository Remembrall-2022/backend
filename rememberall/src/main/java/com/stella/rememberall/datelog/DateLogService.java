package com.stella.rememberall.datelog;

import com.stella.rememberall.datelog.domain.DateLog;
import com.stella.rememberall.datelog.domain.Question;
import com.stella.rememberall.datelog.domain.WeatherInfo;
import com.stella.rememberall.datelog.dto.DateLogResponseDto;
import com.stella.rememberall.datelog.dto.DateLogSaveRequestDto;
import com.stella.rememberall.datelog.dto.DateLogSaveRequestVo;
import com.stella.rememberall.datelog.exception.DateLogExCode;
import com.stella.rememberall.datelog.exception.DateLogException;
import com.stella.rememberall.datelog.exception.QuestionExCode;
import com.stella.rememberall.datelog.exception.QuestionException;
import com.stella.rememberall.datelog.repository.DateLogRepository;
import com.stella.rememberall.datelog.repository.QuestionRepository;
import com.stella.rememberall.placelog.PlaceLogRepository;
import com.stella.rememberall.placelog.PlaceLogSaveRequestDto;
import com.stella.rememberall.placelog.PlaceLogService;
import com.stella.rememberall.tripLog.TripLog;
import com.stella.rememberall.tripLog.TripLogRepository;
import com.stella.rememberall.tripLog.exception.TripLogException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDate;
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
    private final PlaceLogService placeLogService;

    // TODO: 일기 추가하면 경험치, 포인트 주는 로직 개발
    @Transactional
    public Long createDateLog(Long tripLogId, DateLogSaveRequestDto dateLogSaveRequestDto, List<MultipartFile> multipartFileList) {
        TripLog tripLog = getTripLog(tripLogId);
        // 로그인한 회원이 소유자가 맞는지 확인


        LocalDate date = dateLogSaveRequestDto.getDate();
        WeatherInfo weatherInfo = dateLogSaveRequestDto.getWeatherInfo();
        Question question = getQuestionAcceptsNull(dateLogSaveRequestDto.getQuestionId());
        String answer = getAnswerAcceptsNull(dateLogSaveRequestDto);

        validateUniqueDateLog(tripLog, date);
        
        // 길이 제한
        if(tripLog.getDateLogList().size() == 10) return -1L;

        DateLogSaveRequestVo dateLogSaveRequestVo = new DateLogSaveRequestVo(date, weatherInfo, question, answer, tripLog);
        DateLog dateLog = dateLogSaveRequestVo.toEntity();

        // placeLog - userImg
        ArrayList<PlaceLogSaveRequestDto> placeLogList = dateLogSaveRequestDto.getPlaceLogList();

        if(placeLogList == null) return -1L;
        if(placeLogList.size() != multipartFileList.size()) return -1L;
        for(int i=0;i<placeLogList.size();i++){
            placeLogService.savePlaceLog(i, placeLogList.get(i), dateLog, multipartFileList.get(i));
        }

        // triplog 객체의 datelogList 컬렉션 필드에 새로 생성된 datelog 저장
        tripLog.getDateLogList().add(dateLog);
        return dateLogRepository.save(dateLog).getId();
        
        // 경험치 추가
    }

    private TripLog getTripLog(Long tripLogId) {
        TripLog tripLog = tripLogRepository.findById(tripLogId)
                .orElseThrow(() -> new TripLogException(TRIPLOG_NOT_FOUND, "일기장을 찾을 수 없어 일기를 생성할 수 없습니다."));
        return tripLog;
    }

    private Question getQuestionAcceptsNull(Long questionId) {
        Optional<Long> questionIdOptional = Optional.ofNullable(questionId);
        Question question = null;
        if (questionIdOptional.isPresent()) {
            question = questionRepository.findById(questionIdOptional.get())
                    .orElseThrow(() -> new QuestionException(QuestionExCode.QUESTION_NOT_FOUND));
        }
        return question;
    }

    private String getAnswerAcceptsNull(DateLogSaveRequestDto dateLogSaveRequestDto) {
        return Optional.ofNullable(dateLogSaveRequestDto.getAnswer()).orElse(null);
    }

    private void validateUniqueDateLog(TripLog tripLog, LocalDate date) {
        if(dateLogRepository.existsByTripLogAndDate(tripLog, date))
//        List<DateLog> foundDateLog = dateLogRepository.findByTripLogAndDate(tripLog, date);
//        if (!foundDateLog.isEmpty()) {
            throw new DateLogException(DateLogExCode.DUPLICATED_DATELOG);
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

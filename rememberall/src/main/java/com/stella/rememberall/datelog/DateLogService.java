package com.stella.rememberall.datelog;

import com.stella.rememberall.datelog.domain.DateLog;
import com.stella.rememberall.datelog.domain.Question;
import com.stella.rememberall.datelog.dto.DateLogResponseDto;
import com.stella.rememberall.datelog.dto.DateLogSaveRequestDto;
import com.stella.rememberall.datelog.dto.DateLogSaveRequestVo;
import com.stella.rememberall.datelog.exception.DateLogExCode;
import com.stella.rememberall.datelog.exception.DateLogException;
import com.stella.rememberall.datelog.exception.QuestionExCode;
import com.stella.rememberall.datelog.exception.QuestionException;
import com.stella.rememberall.datelog.repository.DateLogRepository;
import com.stella.rememberall.datelog.repository.QuestionRepository;
import com.stella.rememberall.dongdong.DongdongReward;
import com.stella.rememberall.dongdong.DongdongService;
import com.stella.rememberall.placelog.PlaceLog;
import com.stella.rememberall.placelog.PlaceLogResponseDto;
import com.stella.rememberall.placelog.PlaceLogSaveRequestDto;
import com.stella.rememberall.placelog.PlaceLogService;
import com.stella.rememberall.tripLog.TripLog;
import com.stella.rememberall.tripLog.TripLogRepository;
import com.stella.rememberall.tripLog.exception.TripLogException;
import com.stella.rememberall.user.UserService;
import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.userLogImg.UserLogImg;
import com.stella.rememberall.userLogImg.UserLogImgResponseDto;
import com.stella.rememberall.userLogImg.UserLogImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.stella.rememberall.tripLog.exception.TripLogErrorCode.TRIPLOG_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class DateLogService {

    private final DateLogRepository dateLogRepository;
    private final TripLogRepository tripLogRepository;
    private final QuestionRepository questionRepository;
    private final PlaceLogService placeLogService;
    private final UserService userService;
    private final UserLogImgService userLogImgService;
    private final DongdongService dongdongService;

    // TODO: 일기 추가하면 경험치, 포인트 주는 로직 개발
    @Transactional
    public Long createDateLog(Long tripLogId, DateLogSaveRequestDto dateLogSaveRequestDto, List<MultipartFile> multipartFileList) {
        TripLog tripLog = getTripLog(tripLogId);
        checkLoginedUserIsTripLogOwner(tripLog.getUser());

        LocalDate date = dateLogSaveRequestDto.getDate();
        Question question = getQuestionAcceptsNull(dateLogSaveRequestDto.getQuestionId());
        ArrayList<PlaceLogSaveRequestDto> placeLogList = dateLogSaveRequestDto.getPlaceLogList();

        validateUniqueDateLog(tripLog, date);
        checkPlaceLogCountExceeds(placeLogList);

        DateLog dateLog = dateLogRepository.save(getDateLog(dateLogSaveRequestDto, tripLog, question));
        savePlaceLogs(multipartFileList, placeLogList, dateLog);

        // 리워드 지급
        Optional<String> answerOptional = Optional.ofNullable(dateLogSaveRequestDto.getAnswer());
        if (getAnswerAcceptsNull(dateLogSaveRequestDto).length() < 150)
            dongdongService.reward(tripLog.getUser(), DongdongReward.DATELOG_S);
        else
            dongdongService.reward(tripLog.getUser(), DongdongReward.DATELOG);

        return dateLog.getId();
    }

    private TripLog getTripLog(Long tripLogId) {
        TripLog tripLog = tripLogRepository.findById(tripLogId)
                .orElseThrow(() -> new TripLogException(TRIPLOG_NOT_FOUND, "일기장을 찾을 수 없어 일기를 생성할 수 없습니다."));
        return tripLog;
    }

    private void checkLoginedUserIsTripLogOwner(User tripLogOwner) {
        if(!tripLogOwner.equals(userService.getLoginedUser()))
            throw new DateLogException(DateLogExCode.NO_AUTHORIZATION);
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

    private void validateUniqueDateLog(TripLog tripLog, LocalDate date) {
        if(dateLogRepository.existsByTripLogAndDate(tripLog, date))
            throw new DateLogException(DateLogExCode.DUPLICATED_DATELOG);
    }

    private void checkPlaceLogCountExceeds(ArrayList<PlaceLogSaveRequestDto> placeLogList) {
        if(placeLogList.size() > 10) throw new DateLogException(DateLogExCode.COUNT_EXCEED);
    }

    private DateLog getDateLog(DateLogSaveRequestDto dateLogSaveRequestDto, TripLog tripLog, Question question) {
        DateLogSaveRequestVo dateLogSaveRequestVo = new DateLogSaveRequestVo(dateLogSaveRequestDto);
        dateLogSaveRequestVo.setTripLog(tripLog);
        dateLogSaveRequestVo.setQuestion(question);
        return dateLogSaveRequestVo.toEntity();
    }

    private void savePlaceLogs(List<MultipartFile> multipartFileList, ArrayList<PlaceLogSaveRequestDto> placeLogList, DateLog dateLog) {
        if(placeLogList != null) {
            checkCountMatches(multipartFileList.size(), placeLogList.size());
            for (int i = 0; i < placeLogList.size(); i++) {
                placeLogService.savePlaceLog(i, placeLogList.get(i), dateLog, multipartFileList.get(i));
            }
        }
    }

    private void checkCountMatches(int multipartFileListSize, int placeLogListSize) {
        if (multipartFileListSize != placeLogListSize) throw new DateLogException(DateLogExCode.COUNT_NOT_MATCH);
    }

    private String getAnswerAcceptsNull(DateLogSaveRequestDto dateLogSaveRequestDto) {
        return Optional.ofNullable(dateLogSaveRequestDto.getAnswer()).orElse("");
    }

    @Transactional
    public DateLogResponseDto readDateLogFromTripLog(Long dateLogId, Long tripLogId) {
        TripLog tripLog = getTripLog(tripLogId);
        checkLoginedUserIsTripLogOwner(tripLog.getUser());
        DateLog dateLog = getDateLog(dateLogId);
        checkDateLogBelongToTripLog(dateLog, tripLog);
        return getDateLogResponseDto(dateLog);
    }

    private DateLogResponseDto getDateLogResponseDto(DateLog dateLog) {
        DateLogResponseDto dateLogResponseDto = DateLogResponseDto.of(dateLog);
        dateLogResponseDto.setPlaceLogList(getPlaceLogResponseDtoList(dateLog));
        return dateLogResponseDto;
    }

    private List<PlaceLogResponseDto> getPlaceLogResponseDtoList(DateLog dateLog) {
        List<PlaceLog> placeLogList = dateLog.getPlaceLogList();
        List<PlaceLogResponseDto> placeLogResponseDtoList = new ArrayList<>();
        if(placeLogList!=null) {
            for (PlaceLog placeLog : placeLogList) {
                placeLogResponseDtoList.add(
                        createPlaceLogResponseDto(placeLog, sortUserLogImgsByIndex(placeLog.getUserLogImgList()))
                );
            }
        }
        return placeLogResponseDtoList;
    }

    private PlaceLogResponseDto createPlaceLogResponseDto(PlaceLog placeLog, List<UserLogImg> sortedUserLogImgList) {
        List<UserLogImgResponseDto> userLogImgResponseDtos = new ArrayList<>();
        for(UserLogImg userLogImg: sortedUserLogImgList){
            userLogImgResponseDtos.add(UserLogImgResponseDto.of(userLogImg.getIndex(), userLogImgService.getImgUrl(userLogImg.getFileKey())));
        }
        PlaceLogResponseDto responseDto = PlaceLogResponseDto.of(placeLog);
        responseDto.updateUserLogImgListWithImgUrl(userLogImgResponseDtos);
        return responseDto;
    }

    private List<UserLogImg> sortUserLogImgsByIndex(List<UserLogImg> userLogImgList) {
        return userLogImgList.stream().sorted(Comparator.comparing(UserLogImg::getIndex))
                .collect(Collectors.toList());
    }

    private DateLog getDateLog(Long dateLogId) {
        DateLog dateLog = dateLogRepository.findById(dateLogId)
                .orElseThrow(() -> new DateLogException(DateLogExCode.DATELOG_NOT_FOUND, "일기를 찾을 수 없어 조회할 수 없습니다."));
        return dateLog;
    }

    private void checkDateLogBelongToTripLog(DateLog dateLog, TripLog tripLog) {
        if(!tripLog.getDateLogList().contains(dateLog))
            throw new DateLogException(DateLogExCode.DATELOG_NOT_BELONG_TO_TRIPLOG);
    }

    @Transactional
    public void deleteDateLog(Long dateLogId, Long tripLogId) {
        TripLog tripLog = getTripLog(tripLogId);
        checkLoginedUserIsTripLogOwner(tripLog.getUser());
        DateLog dateLog = getDateLog(dateLogId);
        checkDateLogBelongToTripLog(dateLog, tripLog);

        List<PlaceLog> placeLogList = dateLog.getPlaceLogList();
        for(PlaceLog placeLog:placeLogList){
            placeLogService.deletePlaceLog(placeLog);
        }

        dateLogRepository.deleteById(dateLogId);
    }
}

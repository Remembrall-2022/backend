package com.stella.rememberall.datelog;

import com.stella.rememberall.datelog.domain.DateLog;
import com.stella.rememberall.datelog.domain.Question;
import com.stella.rememberall.datelog.dto.*;
import com.stella.rememberall.datelog.exception.DateLogExCode;
import com.stella.rememberall.datelog.exception.DateLogException;
import com.stella.rememberall.datelog.exception.QuestionExCode;
import com.stella.rememberall.datelog.exception.QuestionException;
import com.stella.rememberall.datelog.repository.DateLogRepository;
import com.stella.rememberall.datelog.repository.QuestionRepository;
import com.stella.rememberall.dongdong.DongdongReward;
import com.stella.rememberall.dongdong.DongdongService;
import com.stella.rememberall.placelog.*;
import com.stella.rememberall.placelog.exception.PlaceLogErrorCode;
import com.stella.rememberall.placelog.exception.PlaceLogException;
import com.stella.rememberall.tripLog.TripLog;
import com.stella.rememberall.tripLog.TripLogRepository;
import com.stella.rememberall.tripLog.exception.TripLogException;
import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.userLogImg.UserLogImg;
import com.stella.rememberall.userLogImg.UserLogImgResponseDto;
import com.stella.rememberall.userLogImg.UserLogImgService;
import com.stella.rememberall.userLogImg.exception.EmptyFileException;
import com.stella.rememberall.userLogImg.exception.FileErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.stella.rememberall.tripLog.exception.TripLogErrorCode.TRIPLOG_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class DateLogService {

    private final DateLogRepository dateLogRepository;
    private final TripLogRepository tripLogRepository;
    private final QuestionRepository questionRepository;
    private final PlaceLogService placeLogService;
    private final UserLogImgService userLogImgService;
    private final DongdongService dongdongService;

    @Transactional
    public Long createDateLog(Long tripLogId, DateLogSaveRequestDto dateLogSaveRequestDto, List<MultipartFile> multipartFileList, User loginedUser) {
        TripLog tripLog = getTripLog(tripLogId);
        checkLoginedUserIsTripLogOwner(tripLog.getUser(), loginedUser);

        LocalDate date = dateLogSaveRequestDto.getDate();
        validateUniqueDateLog(tripLog, date);
        checkMultipartFileListCountExceeds(multipartFileList);
//        checkRequestDateIsBetweenTripLogDate(tripLog, date);

        Question question = getQuestionAcceptsNull(dateLogSaveRequestDto.getQuestionId());
        ArrayList<PlaceLogSaveRequestDto> placeLogList = dateLogSaveRequestDto.getPlaceLogList();

        if(placeLogList != null) {
            checkPlaceLogCountExceeds(placeLogList);
            checkPlaceNameDuplicate(placeLogList);
        }

        DateLog dateLog = dateLogRepository.save(getDateLog(dateLogSaveRequestDto, tripLog, question));
        savePlaceLogs(multipartFileList, placeLogList, dateLog);

        chooseRewards(dateLogSaveRequestDto, loginedUser);

        return dateLog.getId();
    }

    private void chooseRewards(DateLogSaveRequestDto dateLogSaveRequestDto, User loginedUser) {
        // 리워드 지급
        Optional<String> answerOptional = Optional.ofNullable(dateLogSaveRequestDto.getAnswer());
        if (getAnswerAcceptsNull(dateLogSaveRequestDto).length() < 150)
            dongdongService.reward(DongdongReward.DATELOG_S, loginedUser);
        else
            dongdongService.reward(DongdongReward.DATELOG, loginedUser);
    }

    public List<SpotResponseDto> getSpotListFromTripLog(Long tripLogId, User loginedUser) {
        TripLog tripLog = getTripLog(tripLogId);
        checkLoginedUserIsTripLogOwner(tripLog.getUser(), loginedUser);
        List<SpotResponseDto> tripLogResult = new ArrayList<>();
        for (DateLog dateLog : tripLog.getDateLogList()) {
            List<SpotResponseDto> spotListFromDateLog = getSpotListFromDateLog(tripLogId, dateLog.getId(), loginedUser);
            tripLogResult = Stream.of(tripLogResult, spotListFromDateLog)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }
        return tripLogResult;
    }

    public List<SpotResponseDto> getDistinctSpotListFromTripLog(Long tripLogId, User loginedUser) {
        //중복 존재하는 별자리지도 찍기
        List<SpotResponseDto> spotListFromTripLog = getSpotListFromTripLog(tripLogId, loginedUser);

        //중복제거
        return spotListFromTripLog.stream().distinct().collect(Collectors.toList());
    }



    public List<SpotResponseDto> getSpotListFromDateLog(Long tripLogId, Long dateLogId, User loginedUser) {
        TripLog tripLog = getTripLog(tripLogId);
        checkLoginedUserIsTripLogOwner(tripLog.getUser(), loginedUser);
        DateLog dateLog = getDateLog(dateLogId);
        checkDateLogBelongToTripLog(dateLog, tripLog);

        return getSpotResponseDtos(dateLog.getPlaceLogList());
    }

    private List<SpotResponseDto> getSpotResponseDtos(List<PlaceLog> placeLogList) {
        List<SpotResponseDto> result = new ArrayList<>();
        for (PlaceLog placeLog : placeLogList) {
            result.add(SpotResponseDto.of(placeLog));
        }
        Collections.sort(result, new SpotListIndexComparator());
        return result;
    }

    private TripLog getTripLog(Long tripLogId) {
        TripLog tripLog = tripLogRepository.findById(tripLogId)
                .orElseThrow(() -> new TripLogException(TRIPLOG_NOT_FOUND, "일기장을 찾을 수 없습니다."));
        return tripLog;
    }

    private void checkLoginedUserIsTripLogOwner(User tripLogOwner, User loginedUser) {
        if(!tripLogOwner.equals(loginedUser))
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

    private void checkMultipartFileListCountExceeds(List<MultipartFile> multipartFileList) {
        if(multipartFileList.size() > 10) throw new DateLogException(DateLogExCode.COUNT_EXCEED, "파일 개수는 10개를 초과할 수 없습니다.");
    }

    private void checkRequestDateIsBetweenTripLogDate(TripLog tripLog, LocalDate requestDate) {
        LocalDate tripStartDate = tripLog.getTripStartDate();
        LocalDate tripEndDate = tripLog.getTripEndDate();
        DateRangeValidator checker = new DateRangeValidator(tripStartDate, tripEndDate);
        if(!checker.isWithinRange(requestDate))
            throw new DateLogException(DateLogExCode.INVALID_DATE);
    }

    private void checkPlaceNameDuplicate(ArrayList<PlaceLogSaveRequestDto> placeLogList) {
        ArrayList<String> placeLogNameList = new ArrayList<>();
        for(PlaceLogSaveRequestDto dto:placeLogList) placeLogNameList.add(dto.getPlaceInfo().getName());
        Set<String> set = new HashSet<>(placeLogNameList);
        if (set.size() != placeLogList.size()) {
            throw new DateLogException(DateLogExCode.DUPLICATED_PLACEID);
        }
    }

    private DateLog getDateLog(DateLogSaveRequestDto dateLogSaveRequestDto, TripLog tripLog, Question question) {
        DateLogSaveRequestVo dateLogSaveRequestVo = new DateLogSaveRequestVo(dateLogSaveRequestDto);
        dateLogSaveRequestVo.setTripLog(tripLog);
        dateLogSaveRequestVo.setQuestion(question);
        return dateLogSaveRequestVo.toEntity();
    }

    private void savePlaceLogs(List<MultipartFile> multipartFileList, ArrayList<PlaceLogSaveRequestDto> placeLogList, DateLog dateLog) {
        if(placeLogList == null) return;
        for(PlaceLogSaveRequestDto placeLogSaveRequestDto:placeLogList){
            placeLogService.savePlaceLog(
                    placeLogList.indexOf(placeLogSaveRequestDto),
                    placeLogSaveRequestDto,
                    dateLog,
                    getMultiPartFile(placeLogSaveRequestDto.getImgName(), multipartFileList)
            );
        }
    }

    private MultipartFile getMultiPartFile(String imgName, List<MultipartFile> multipartFileList) {
        if(imgName.compareTo("")==0) return null;
        for(MultipartFile multipartFile:multipartFileList){
            if(multipartFile.getOriginalFilename().compareTo(imgName)==0){
                return multipartFile;
            }
        }
        throw new EmptyFileException(FileErrorCode.FILE_NOT_FOUND, "요청한 파일명을 가진 이미지를 보내지 않았습니다.");
    }


    private void checkCountMatches(int multipartFileListSize, int placeLogListSize) {
        if (multipartFileListSize != placeLogListSize) throw new DateLogException(DateLogExCode.COUNT_NOT_MATCH);
    }

    private String getAnswerAcceptsNull(DateLogSaveRequestDto dateLogSaveRequestDto) {
        return Optional.ofNullable(dateLogSaveRequestDto.getAnswer()).orElse("");
    }

    @Transactional
    public DateLogResponseDto readDateLogFromTripLog(Long dateLogId, Long tripLogId, User loginedUser) {
        TripLog tripLog = getTripLog(tripLogId);
        checkLoginedUserIsTripLogOwner(tripLog.getUser(), loginedUser);
        DateLog dateLog = getDateLog(dateLogId);
        checkDateLogBelongToTripLog(dateLog, tripLog);

        //별자리지도 setting
        DateLogResponseDto dateLogResponseDto = getDateLogResponseDto(dateLog);
        dateLogResponseDto.setConstellationMapFromDateLog(getSpotListFromDateLog(tripLogId, dateLogId, loginedUser));

        return dateLogResponseDto;
    }

    private DateLogResponseDto getDateLogResponseDto(DateLog dateLog) {
        DateLogResponseDto dateLogResponseDto = DateLogResponseDto.of(dateLog);
        dateLogResponseDto.setPlaceLogList(getPlaceLogResponseDtoList(dateLog));
        return dateLogResponseDto;
    }

    private List<PlaceLogResponseDto> getPlaceLogResponseDtoList(DateLog dateLog) {
        List<PlaceLog> placeLogList = dateLog.getPlaceLogList();
        List<PlaceLogResponseDto> placeLogResponseDtoList = new ArrayList<>();
        if(placeLogList.size()==0) return null;

        for (PlaceLog placeLog : placeLogList) {
            placeLogResponseDtoList.add(createPlaceLogResponseDto(placeLog, placeLog.getUserLogImgList()));
        }
        Collections.sort(placeLogResponseDtoList, new ListIndexComparator());

        return placeLogResponseDtoList;
    }

    private PlaceLogResponseDto createPlaceLogResponseDto(PlaceLog placeLog, List<UserLogImg> userLogImgList) {
        List<UserLogImgResponseDto> userLogImgResponseDtos = new ArrayList<>();
        for(UserLogImg userLogImg: userLogImgList) {
            userLogImgResponseDtos.add(UserLogImgResponseDto.of(userLogImg.getId(), userLogImgService.getImgUrl(userLogImg.getFileKey())));
        }
        PlaceLogResponseDto responseDto = PlaceLogResponseDto.of(placeLog);
        if(!userLogImgResponseDtos.isEmpty()) responseDto.updateUserLogImgWithImgUrl(userLogImgResponseDtos.get(0));
        return responseDto;
    }

//    이미지 여러개
//    private PlaceLogResponseDto createPlaceLogResponseDto(PlaceLog placeLog, List<UserLogImg> sortedUserLogImgList) {
//        List<UserLogImgResponseDto> userLogImgResponseDtos = new ArrayList<>();
//        for(UserLogImg userLogImg: sortedUserLogImgList){
//            userLogImgResponseDtos.add(UserLogImgResponseDto.of(userLogImg.getIndex(), userLogImgService.getImgUrl(userLogImg.getFileKey())));
//        }
//        PlaceLogResponseDto responseDto = PlaceLogResponseDto.of(placeLog);
//        responseDto.updateUserLogImgListWithImgUrl(userLogImgResponseDtos);
//        return responseDto;
//    }

    private List<UserLogImg> sortUserLogImgsByIndex(List<UserLogImg> userLogImgList) {
        return userLogImgList.stream().sorted(Comparator.comparing(UserLogImg::getIndex))
                .collect(Collectors.toList());
    }

    private DateLog getDateLog(Long dateLogId) {
        DateLog dateLog = dateLogRepository.findById(dateLogId)
                .orElseThrow(() -> new DateLogException(DateLogExCode.DATELOG_NOT_FOUND, "날짜별 일기를 찾을 수 없습니다."));
        return dateLog;
    }

    private void checkDateLogBelongToTripLog(DateLog dateLog, TripLog tripLog) {
        if(!tripLog.getDateLogList().contains(dateLog))
            throw new DateLogException(DateLogExCode.DATELOG_NOT_BELONG_TO_TRIPLOG);
    }

    @Transactional
    public void deleteDateLog(Long dateLogId, Long tripLogId, User loginedUser) {
        TripLog tripLog = getTripLog(tripLogId);
        checkLoginedUserIsTripLogOwner(tripLog.getUser(), loginedUser);
        DateLog dateLog = getDateLog(dateLogId);
        checkDateLogBelongToTripLog(dateLog, tripLog);

        List<PlaceLog> placeLogList = dateLog.getPlaceLogList();
//        for(PlaceLog placeLog:placeLogList){
//            placeLogService.deletePlaceLog(placeLog.getId());
//        }

        dateLogRepository.deleteById(dateLogId);
    }

    @Transactional
    public void updateDate(Long dateLogId, DateUpdateRequestDto requestDto) {
        DateLog dateLog = getDateLog(dateLogId);
        validateUniqueDateLog(dateLog.getTripLog(), requestDto.getDate());
        checkRequestDateIsBetweenTripLogDate(dateLog.getTripLog(), requestDto.getDate());
        dateLog.updateDate(requestDto.getDate());
    }

    @Transactional
    public void updateQnA(Long dateLogId, QnAUpdateRequestDto requestDto) {
        DateLog dateLog = getDateLog(dateLogId);
        dateLog.updateQuestion(getQuestionAcceptsNull(requestDto.getQuestionId()));
        dateLog.updateAnswer(requestDto.getAnswer());
    }

    @Transactional
    public void updateWeatherInfo(Long dateLogId, WeatherInfoUpdateRequestDto requestDto){
        DateLog dateLog = getDateLog(dateLogId);
        dateLog.updateWeatherInfo(requestDto.getWeatherInfo());
    }

    @Transactional
    public void updatePlaceLogIndex(Long dateLogId, PlaceLogIndexUpdateRequestDto indexInfo) throws PlaceLogException, DateLogException {
        DateLog dateLog = getDateLog(dateLogId);
        List<PlaceLog> placeLogList = dateLog.getPlaceLogList();
        List<PlaceLog> responseList = new ArrayList<>();

        Map<String, Integer> indexAndPlaceLogIdMap = indexInfo.getIndexAndPlaceLogIds();
        indexAndPlaceLogIdMap.keySet();

        checkPlaceLogUpdateRequestCountMatches(indexInfo, placeLogList);
        checkPlaceLogIdValid(placeLogList, indexInfo.getPlaceLogIdList());

        ArrayList<Integer> indexes = new ArrayList<>(indexAndPlaceLogIdMap.values());
        checkIndexInfoValid(indexes);
        updateIndexOfPlaceLogList(placeLogList, responseList, indexAndPlaceLogIdMap);
    }

    private void checkPlaceLogUpdateRequestCountMatches(PlaceLogIndexUpdateRequestDto indexInfo, List<PlaceLog> placeLogList) {
        if(indexInfo.getIndexAndPlaceLogIds().size() != placeLogList.size()){
            throw new DateLogException(DateLogExCode.COUNT_NOT_MATCH, "요청한 인덱스 수정 리스트의 사이즈와 실제 관광지별 일기의 개수가 일치하지 않습니다.");
        }
    }

    private void checkPlaceLogIdValid(List<PlaceLog> placeLogList, List<Long> placeLogIdList) {
        for(PlaceLog placeLog:placeLogList){
            if(!placeLogIdList.contains(placeLog.getId()))
                throw new PlaceLogException(PlaceLogErrorCode.PLACELOG_NOT_BELONG_TO_DATELOG);
        }
    }

    private void checkIndexInfoValid(ArrayList<Integer> indexInfo) {
        if (indexInfo.size() <= 1) {
            return;
        }
        
        // 정렬
        Integer min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (int i: indexInfo){
            if (i < min) { min = i; }
            if (i > max) { max = i; }
        }

        // 최소값이 0
        if(min != 0) throw new DateLogException(DateLogExCode.INDEX_ERROR, "요청한 인덱스가 0으로 시작하지 않습니다.");

        // 중복 없음
        Set<Integer> visited = new HashSet<>();
        for (int i: indexInfo) {
            if (visited.contains(i)) {
                throw new DateLogException(DateLogExCode.INDEX_ERROR, "요청한 인덱스에 중복이 있습니다.");
            }
            visited.add(i);
        }

        // 최대-최소 == n-1
        if (max - min != indexInfo.size() - 1) {
            throw new DateLogException(DateLogExCode.INDEX_ERROR, "요청한 인덱스의 끝값을 다시 확인해주세요.");
        }

    }

    private void updateIndexOfPlaceLogList(List<PlaceLog> placeLogList, List<PlaceLog> responseList, Map<String, Integer> indexAndPlaceLogIdMap) {
        for(PlaceLog placeLog: placeLogList){
            Integer newIndex = indexAndPlaceLogIdMap.get(placeLog.getId().toString());
            placeLog.updateIndex(newIndex);
            responseList.add(placeLog);
        }
    }

}

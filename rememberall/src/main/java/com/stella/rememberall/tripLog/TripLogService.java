package com.stella.rememberall.tripLog;

import com.stella.rememberall.common.exception.jpa.CommonJpaErrorCode;
import com.stella.rememberall.common.exception.jpa.CommonJpaException;
import com.stella.rememberall.tripLog.dto.TripLogResponseDto;
import com.stella.rememberall.tripLog.dto.TripLogSaveRequestDto;
import com.stella.rememberall.tripLog.dto.TripLogUpdateRequestDto;
import com.stella.rememberall.tripLog.exception.TripLogException;
import com.stella.rememberall.user.UserService;
import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.tripLog.exception.TripLogErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TripLogService {

    private final TripLogRepository tripLogRepository;
    private final UserService userService;

    @Transactional
    public TripLogResponseDto saveTripLog(TripLogSaveRequestDto saveRequestDto){
        User loginedUser = userService.getLoginedUser();
        TripLog savedTripLog = saveTripLogWithUser(saveRequestDto, loginedUser); // TODO : save 예외 잡기는 했는데 더 좋은 방법이 없을까? save fail은 500이고 find fail은 400 에러인디
        // TODO : 디폴트 이미지 지정 방법 논의 후 이미지 setter
        return TripLogResponseDto.of(savedTripLog);
    }

    @Transactional
    private TripLog saveTripLogWithUser(TripLogSaveRequestDto dto, User loginedUser) {
        try {
            return tripLogRepository.save(dto.toEntityWithUser(loginedUser));
        } catch (IllegalArgumentException e) {
            throw new CommonJpaException(CommonJpaErrorCode.SAVE_FAIL);
        }
    }

    @Transactional
    public TripLogResponseDto findTripLog(Long id){
        return TripLogResponseDto.of(findTripLogById(id));
    }

    private TripLog findTripLogById(Long id) {
        return tripLogRepository.findById(id)
                .orElseThrow(() -> new TripLogException(TripLogErrorCode.TRIPLOG_NOT_FOUND));
    }

    @Transactional
    public List<TripLogResponseDto> findTripLogList(){
        User loginedUser = userService.getLoginedUser();
        List<TripLog> entityList = tripLogRepository.findAllByUser(loginedUser);
        return mapEntityListToDtoList(entityList);
    }

    private List<TripLogResponseDto> mapEntityListToDtoList(List<TripLog> foundTripLogList) {
        return foundTripLogList.stream().map(TripLogResponseDto::of).collect(Collectors.toList());
    }



    @Transactional
    public TripLogResponseDto updateTripLog(TripLogUpdateRequestDto updateRequestDto, Long tripLogId){
        User loginedUser = userService.getLoginedUser();
        // TODO : Spring Security로 수정 권한 확인하도록 수정
        checkUserAuthorityToUpdateTripLog(loginedUser, findTripLogById(tripLogId));
        TripLog updatedTripLog = updateTripLogWithUser(updateRequestDto, loginedUser, tripLogId);
        // TODO : 디폴트 이미지 지정 방법 논의 후 이미지 setter
        return TripLogResponseDto.of(updatedTripLog);
    }

    private void checkUserAuthorityToUpdateTripLog(User loginedUser, TripLog foundTripLog) {
        if(foundTripLog.getUser().getId() != loginedUser.getId()) {
            throw new TripLogException(TripLogErrorCode.NO_AUTHORITY_TO_UPDATE);
        }
    }

    @Transactional
    private TripLog updateTripLogWithUser(TripLogUpdateRequestDto dto, User loginedUser, Long tripLogId) {
        try {
            return tripLogRepository.save(dto.toEntityWithUserAndId(loginedUser, tripLogId));
        } catch (IllegalArgumentException e) {
            throw new CommonJpaException(CommonJpaErrorCode.SAVE_FAIL); // TODO : save 예외 잡기는 했는데 더 좋은 방법이 없을까? save fail은 500이고 find fail은 400 에러인디
        }
    }

}

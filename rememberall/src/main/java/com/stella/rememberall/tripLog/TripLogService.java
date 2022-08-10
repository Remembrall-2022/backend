package com.stella.rememberall.tripLog;

import com.stella.rememberall.security.SecurityUtil;
import com.stella.rememberall.tripLog.dto.TripLogResponseDto;
import com.stella.rememberall.tripLog.dto.TripLogSaveRequestDto;
import com.stella.rememberall.tripLog.exception.TripLogException;
import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.user.exception.MemberException;
import com.stella.rememberall.user.exception.MyErrorCode;
import com.stella.rememberall.user.exception.TripLogErrorCode;
import com.stella.rememberall.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Transactional
    public TripLogResponseDto saveTripLog(TripLogSaveRequestDto dto){
        User loginedUser = getLoginedUser();
        // TODO : 레포지토리 저장 실패 예외 처리
        TripLog savedTripLog = tripLogRepository.save(dto.toEntityWithUser(loginedUser));
        // TODO : 디폴트 이미지 지정 방법 논의 후 이미지 setter
        return TripLogResponseDto.of(savedTripLog);
    }

    @Transactional
    public TripLogResponseDto findTripLog(Long id){
        TripLog foundTripLog = tripLogRepository.findById(id)
                .orElseThrow(() -> new TripLogException(TripLogErrorCode.TRIPLOG_NOT_FOUND));
        return TripLogResponseDto.of(foundTripLog);
    }

    @Transactional
    public List<TripLogResponseDto> findTripLogList(){
        User loginedUser = getLoginedUser();
        List<TripLog> entityList = tripLogRepository.findAllByUser(loginedUser);
        return mapEntityListToDtoList(entityList);
    }

    private List<TripLogResponseDto> mapEntityListToDtoList(List<TripLog> foundTripLogList) {
        return foundTripLogList.stream().map(TripLogResponseDto::of).collect(Collectors.toList());
    }

    private User getLoginedUser(){
        return SecurityUtil.getCurrentUserPk().flatMap(userRepository::findById)
                .orElseThrow(() -> new MemberException(MyErrorCode.ENTITY_NOT_FOUND));
    }


}

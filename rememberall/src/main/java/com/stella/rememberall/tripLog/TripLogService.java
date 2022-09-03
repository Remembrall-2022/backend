package com.stella.rememberall.tripLog;

import com.stella.rememberall.common.exception.jpa.CommonJpaErrorCode;
import com.stella.rememberall.common.exception.jpa.CommonJpaException;
import com.stella.rememberall.datelog.DateLogService;
import com.stella.rememberall.datelog.domain.DateLog;
import com.stella.rememberall.datelog.dto.DateLogResponseDto;
import com.stella.rememberall.tripLog.dto.*;
import com.stella.rememberall.tripLog.exception.TripLogException;
import com.stella.rememberall.user.LoginedUserService;
import com.stella.rememberall.user.UserService;
import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.tripLog.exception.TripLogErrorCode;
import com.stella.rememberall.userLogImg.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TripLogService {

    private final TripLogRepository tripLogRepository;
    private final LoginedUserService loginedUserService;
    private final DateLogService dateLogService;
    private final S3Util s3Util;

    @Transactional
    public Long saveTripLog(TripLogSaveRequestDto saveRequestDto){
        User loginedUser = loginedUserService.getLoginedUser();
        TripLog savedTripLog = saveTripLogWithUser(saveRequestDto, loginedUser); // TODO : save 예외 잡기는 했는데 더 좋은 방법이 없을까? save fail은 500이고 find fail은 400 에러인디
        // TODO : 디폴트 이미지 지정 방법 논의 후 이미지 setter
        return savedTripLog.getId();
    }

    @Transactional
    private TripLog saveTripLogWithUser(TripLogSaveRequestDto dto, User loginedUser) {
        try {
            return tripLogRepository.save(dto.toEntityWithUser(loginedUser));
        } catch (IllegalArgumentException e) {
            throw new CommonJpaException(CommonJpaErrorCode.SAVE_FAIL);
        }
    }

    // 오래된 날짜 순으로 정렬
    @Transactional
    public TripLogWithPlaceLogIdListResponseDto findTripLogWithPlaceLogIdList(Long id){
        TripLogWithPlaceLogIdListResponseDto responseDto = TripLogWithPlaceLogIdListResponseDto.of(findTripLogById(id));

        //별자리지도 setting
        responseDto.setConstellationMapFromTripLog(dateLogService.getSpotListFromTripLog(id));
        responseDto.setDistinctConstellationMapFromTripLog(dateLogService.getDistinctSpotListFromTripLog(id));
        return responseDto;
    }

    // 오래된 날짜 순으로 정렬
    @Transactional
    public TripLogWithWholePlaceLogListResponseDto findTripLogWithWholePlaceLogList(Long tripLogId){
        TripLog tripLogById = findTripLogById(tripLogId);
        TripLogWithWholePlaceLogListResponseDto responseDto = TripLogWithWholePlaceLogListResponseDto.of(tripLogById);
        List<DateLogResponseDto> dateLogResponseDtoList = getDateLogResponseDtoList(tripLogId, tripLogById);
        responseDto.setDateLogResponseDtoList(dateLogResponseDtoList);

        //별자리지도 setting
        responseDto.setConstellationMapFromTripLog(dateLogService.getSpotListFromTripLog(tripLogId));
        responseDto.setDistinctConstellationMapFromTripLog(dateLogService.getDistinctSpotListFromTripLog(tripLogId));

        return responseDto;
    }

    private List<DateLogResponseDto> getDateLogResponseDtoList(Long tripLogId, TripLog tripLogById) {
        Collections.sort(tripLogById.getDateLogList(), new OldCreatedDateListComparator());
        List<DateLogResponseDto> dateLogResponseDtoList = new ArrayList<>();
        for(DateLog dateLog: tripLogById.getDateLogList()) {
            dateLogResponseDtoList.add(dateLogService.readDateLogFromTripLog(dateLog.getId(), tripLogId));
        }
        return dateLogResponseDtoList;
    }

    private TripLog findTripLogById(Long id) {
        return tripLogRepository.findById(id)
                .orElseThrow(() -> new TripLogException(TripLogErrorCode.TRIPLOG_NOT_FOUND));
    }

    @Transactional
    public List<TripLogSimpleResponseDto> findTripLogList(){
        User loginedUser = loginedUserService.getLoginedUser();
        List<TripLog> entityList = tripLogRepository.findAllByUser(loginedUser);
        Collections.sort(entityList, new RecentCreatedDateListComparator());
        return mapEntityListToDtoListWithDefaultImg(entityList);
    }

    // 이미지를 인덱스별로 지정
    private List<TripLogSimpleResponseDto> mapEntityListToDtoListWithDefaultImg(List<TripLog> foundTripLogList) {
        List<TripLogSimpleResponseDto> responseDtoList = new ArrayList<>();
        TripLogDefaultImageUtil imageUtil = new TripLogDefaultImageUtil(s3Util);
        for(int i=0;i<foundTripLogList.size();i++){
            String imgUrl = imageUtil.getImgUrl(i % imageUtil.getImgListSize());
            TripLogSimpleResponseDto responseDto = TripLogSimpleResponseDto.of(foundTripLogList.get(i));
            responseDto.setTripLogImgUrl(imgUrl);
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

    @Transactional
    public Long updateTripLog(TripLogUpdateRequestDto updateRequestDto, Long tripLogId){
        User loginedUser = loginedUserService.getLoginedUser();
        checkUserAuthorityToUpdateTripLog(loginedUser, findTripLogById(tripLogId));
        TripLog updatedTripLog = updateTripLogWithUser(updateRequestDto, loginedUser, tripLogId);
        // TODO : 디폴트 이미지 지정 방법 논의 후 이미지 setter
        return updatedTripLog.getId();
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

    @Transactional
    public void deleteTripLog(Long tripLogId) {
        // datelog 삭제
        TripLog tripLogById = findTripLogById(tripLogId);
        List<DateLog> dateLogList = tripLogById.getDateLogList();
        for(DateLog dateLog:dateLogList){
            dateLogService.deleteDateLog(dateLog.getId(), tripLogId);
        }
        // triplog 삭제
        tripLogRepository.deleteById(tripLogId);
    }

}

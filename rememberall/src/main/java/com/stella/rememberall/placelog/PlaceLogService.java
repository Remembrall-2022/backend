package com.stella.rememberall.placelog;

import com.stella.rememberall.datelog.domain.DateLog;
import com.stella.rememberall.datelog.exception.DateLogExCode;
import com.stella.rememberall.datelog.exception.DateLogException;
import com.stella.rememberall.datelog.repository.DateLogRepository;
import com.stella.rememberall.placelog.exception.PlaceLogErrorCode;
import com.stella.rememberall.placelog.exception.PlaceLogException;
import com.stella.rememberall.userLogImg.UserLogImg;
import com.stella.rememberall.userLogImg.UserLogImgRepository;
import com.stella.rememberall.userLogImg.UserLogImgResponseDto;
import com.stella.rememberall.userLogImg.UserLogImgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlaceLogService {
    private final PlaceLogRepository placeLogRepository;
    private final PlaceService placeService;
    private final UserLogImgService userLogImgService;
    private final UserLogImgRepository userLogImgRepository;
    private final DateLogRepository dateLogRepository;

    @Transactional
    public Long savePlaceLog(Long dateLogId, PlaceLogSaveRequestDto requestDto, List<MultipartFile> multipartFileList){
        checkCommentLength(requestDto.getComment());
        DateLog dateLog = getDateLog(dateLogId);
        placeService.saveOrUpdatePlace(requestDto.getPlaceInfo());
        PlaceLog placeLog = savePlaceLogWithDateLog(requestDto, dateLog);
        userLogImgService.saveUserLogImgList(placeLog, multipartFileList);
        return placeLog.getId();
    }

    private void checkCommentLength(String comment) {
        if(comment.length()>255) throw new DateLogException(DateLogExCode.COMMENT_LENGTH_EXCEED);
    }

    private DateLog getDateLog(Long dateLogId) {
        DateLog dateLog = dateLogRepository.findById(dateLogId)
                .orElseThrow(() -> new DateLogException(DateLogExCode.DATELOG_NOT_FOUND));
        return dateLog;
    }

    private PlaceLog savePlaceLogWithDateLog(PlaceLogSaveRequestDto requestDto, DateLog dateLog) {
        checkCommentLength(requestDto.getComment());
        PlaceLog toEntity = requestDto.toEntity();
        toEntity.setDateLog(dateLog);
        toEntity.setIndex(dateLog.getPlaceLogList().size());
        PlaceLog placeLog = placeLogRepository.save(toEntity);
        return placeLog;
    }

    @Transactional
    public void savePlaceLog(int index, PlaceLogSaveRequestDto requestDto, DateLog dateLog, MultipartFile multipartFile){
        checkCommentLength(requestDto.getComment());
        Place place = placeService.saveOrUpdatePlace(requestDto.getPlaceInfo());
        PlaceLog placeLog = requestDto.toEntity();
        placeLog.setPlace(place);
        placeLog.setDateLog(dateLog);
        placeLog.setIndex(index);
        placeLogRepository.save(placeLog);
        userLogImgService.saveUserLogImgAllowsNull(placeLog, multipartFile);
    }

    @Transactional
    public Long savePlaceLog(PlaceLogSaveRequestDto requestDto, DateLog dateLog){
        placeService.saveOrUpdatePlace(requestDto.getPlaceInfo());
        PlaceLog entity = requestDto.toEntity();
        PlaceLog placeLog = placeLogRepository.save(entity.updateDateLog(dateLog));
        return placeLog.getId();
    }

    @Transactional
    public PlaceLogResponseDto getPlaceLog(Long placeLogId) throws PlaceLogException{
        PlaceLog placeLog = findPlaceLog(placeLogId);
        UserLogImg userLogImg = placeLog.getUserLogImgList().get(0);
//        List<UserLogImg> sortedUserLogImgList = sortUserLogImgsByIndex(placeLog.getUserLogImgList());
//        return createPlaceLogResponseDto(placeLog, sortedUserLogImgList);
        return createPlaceLogResponseDto(placeLog, userLogImg);
    }

    private List<UserLogImg> sortUserLogImgsByIndex(List<UserLogImg> userLogImgList) {
        return userLogImgList.stream().sorted(Comparator.comparing(UserLogImg::getIndex))
                .collect(Collectors.toList());
    }

    private PlaceLogResponseDto createPlaceLogResponseDto(PlaceLog placeLog, UserLogImg userLogImg) {
        PlaceLogResponseDto responseDto = PlaceLogResponseDto.of(placeLog);
        UserLogImgResponseDto userLogImgResponseDto = UserLogImgResponseDto.of(userLogImg.getId(), userLogImgService.getImgUrl(userLogImg.getFileKey()));
        responseDto.updateUserLogImgWithImgUrl(userLogImgResponseDto);
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

    private PlaceLog findPlaceLog(Long placeLogId) {
        return placeLogRepository.findById(placeLogId)
                .orElseThrow(() -> new PlaceLogException(PlaceLogErrorCode.NOT_FOUND, "존재하지 않는 관광지 일기입니다."));
    }

    @Transactional
    public void updateUserImg(Long userLogImgId, MultipartFile multipartFile) {
        UserLogImg userLogImg = findUserLogImg(userLogImgId);
        userLogImgService.updateUserLogImg(userLogImg, multipartFile);
    }

    private UserLogImg findUserLogImg(Long userLogImgId) {
        return userLogImgRepository.findById(userLogImgId)
                .orElseThrow(()->new PlaceLogException(PlaceLogErrorCode.NOT_FOUND, "존재하지 않는 이미지 정보입니다."));
    }

    @Transactional
    public Long updatePlaceLogComment(Long placeLogId, String comment){
        PlaceLog placeLog = findPlaceLog(placeLogId).updateComment(comment);
        return placeLog.getId();
    }

    @Transactional
    public Long updatePlace(Long placeLogId, PlaceSaveRequestDto requestDto){
        findPlaceLog(placeLogId).setPlace(placeService.getPlace(requestDto.getPlaceId()));
        return placeService.saveOrUpdatePlace(requestDto).getId();
    }

    @Transactional
    public Long updatePlaceLog(Long placeLogId, PlaceLogSaveRequestDto requestDto){
        placeService.saveOrUpdatePlace(requestDto.getPlaceInfo());
        return placeLogRepository.save(requestDto.toEntityWithId(placeLogId)).getId();
    }
    
    @Transactional
    public Long deletePlaceLog(Long placeLogId) throws PlaceLogException{
        PlaceLog placeLog = findPlaceLog(placeLogId);
        userLogImgService.deleteUserLogImg(placeLog);
        placeLogRepository.deleteById(placeLogId);
//        placeService.deletePlaceIfNotReferenced(placeLog.getPlace().getName());
        return placeLogId;
    }

    @Transactional
    public Long deletePlaceLog(PlaceLog placeLog) throws PlaceLogException{
        userLogImgService.deleteUserLogImg(placeLog);
        placeLogRepository.deleteById(placeLog.getId());
//        placeService.deletePlaceIfNotReferenced(placeLog.getPlace().getName());
        return placeLog.getId();
    }
}
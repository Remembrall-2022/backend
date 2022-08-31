package com.stella.rememberall.placelog;

import com.stella.rememberall.datelog.domain.DateLog;
import com.stella.rememberall.datelog.exception.DateLogExCode;
import com.stella.rememberall.datelog.exception.DateLogException;
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

    @Transactional
    public Long savePlaceLog(PlaceLogSaveRequestDto requestDto, List<MultipartFile> multipartFileList){
        placeService.saveOrUpdatePlace(requestDto.getPlaceInfo());
        PlaceLog placeLog = placeLogRepository.save(requestDto.toEntity());
        userLogImgService.saveUserLogImgList(placeLog, multipartFileList);
        return placeLog.getId();
    }

    @Transactional
    public void savePlaceLog(int index, PlaceLogSaveRequestDto requestDto, DateLog dateLog, MultipartFile multipartFile){
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
    public Long updatePlace(PlaceSaveRequestDto requestDto){
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
        placeService.deletePlaceIfNotReferenced(placeLog.getPlace().getId());
        userLogImgService.deleteUserLogImg(placeLog);
        placeLogRepository.deleteById(placeLogId);
        return placeLogId;
    }

    @Transactional
    public Long deletePlaceLog(PlaceLog placeLog) throws PlaceLogException{
        userLogImgService.deleteUserLogImg(placeLog);
        placeLogRepository.deleteById(placeLog.getId());
        placeService.deletePlaceIfNotReferenced(placeLog.getPlace().getId());
        return placeLog.getId();
    }
}
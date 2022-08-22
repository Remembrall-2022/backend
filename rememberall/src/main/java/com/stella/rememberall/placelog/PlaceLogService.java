package com.stella.rememberall.placelog;

import com.stella.rememberall.placelog.exception.PlaceLogErrorCode;
import com.stella.rememberall.placelog.exception.PlaceLogException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlaceLogService {
    private final PlaceLogRepository placeLogRepository;
    private final PlaceService placeService;

    @Transactional
    public Long savePlaceLog(PlaceLogSaveRequestDto requestDto){
        placeService.saveOrUpdatePlace(requestDto.getPlaceInfo());
        return placeLogRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public PlaceLogResponseDto getPlaceLog(Long placeLogId) throws PlaceLogException{
        PlaceLog placeLog = findPlaceLog(placeLogId);
        return PlaceLogResponseDto.of(placeLog);
    }

    private PlaceLog findPlaceLog(Long placeLogId) {
        return placeLogRepository.findById(placeLogId)
                .orElseThrow(() -> new PlaceLogException(PlaceLogErrorCode.NOT_FOUND, "존재하지 않는 관광지 일기입니다."));
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
        placeLogRepository.deleteById(placeLogId);
        return placeLogId;
    }

}
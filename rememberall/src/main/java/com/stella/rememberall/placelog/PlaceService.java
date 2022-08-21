package com.stella.rememberall.placelog;

import com.stella.rememberall.placelog.exception.PlaceLogErrorCode;
import com.stella.rememberall.placelog.exception.PlaceLogException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlaceService {
    private final PlaceRepository placeRepository;

    public Place saveOrUpdatePlace(PlaceSaveRequestDto requestDto){
        return placeRepository.save(requestDto.toEntity());
    }
    
    public Place getPlace(Long placeId){
        return placeRepository.findById(placeId)
                .orElseThrow(()->new PlaceLogException(PlaceLogErrorCode.NOT_FOUND));
    }
    
    public void deletePlace(Long placeId){
        // TODO : placeLog에서 연관관계가 있는 Place인지 확인
        placeRepository.deleteById(placeId);
    }

}

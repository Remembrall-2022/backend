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
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final PlaceLogRepository placeLogRepository;

    @Transactional
    public Place saveOrUpdatePlace(PlaceSaveRequestDto requestDto){
        return placeRepository.save(requestDto.toEntity());
    }

    @Transactional
    public Place getPlace(Long placeId){
        return placeRepository.findById(placeId)
                .orElseThrow(()->new PlaceLogException(PlaceLogErrorCode.NOT_FOUND));
    }

    @Transactional
    public void deletePlaceIfNotReferenced(Long placeId){
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceLogException(PlaceLogErrorCode.NOT_FOUND));
        if(isNotReferencedInPlaceLog(place))
            placeRepository.deleteById(placeId);
    }

    private boolean isNotReferencedInPlaceLog(Place place) {
        return placeLogRepository.findAllByPlace(place).size() == 0;
    }

}

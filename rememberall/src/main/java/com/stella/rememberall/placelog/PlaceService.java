package com.stella.rememberall.placelog;

import com.stella.rememberall.placelog.exception.PlaceLogErrorCode;
import com.stella.rememberall.placelog.exception.PlaceLogException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final PlaceLogRepository placeLogRepository;

    @Transactional
    public Place saveOrUpdatePlace(PlaceSaveRequestDto requestDto){
        // 동일한 이름이 있으면 update, 없으면 save
        Place savedPlace = new Place();
        Optional<Place> foundPlace = placeRepository.findByName(requestDto.getName());
        if(foundPlace.isEmpty()) savedPlace = placeRepository.save(requestDto.toEntity());
        else{
            savedPlace = foundPlace.get();
            savedPlace = savedPlace.updatePlaceInfo(requestDto.getAddress(), requestDto.getLongitude(), requestDto.getLatitude());
        }
        return savedPlace;
    }

    @Transactional
    public Place getPlace(String placeName){
        return placeRepository.findByName(placeName)
                .orElseThrow(()->new PlaceLogException(PlaceLogErrorCode.NOT_FOUND));
    }

    @Transactional
    public void deletePlaceIfNotReferenced(String placeName){
        Place place = placeRepository.findByName(placeName)
                .orElseThrow(() -> new PlaceLogException(PlaceLogErrorCode.NOT_FOUND));
        if(isNotReferencedInPlaceLog(place)) {
            Long aLong = placeRepository.deleteByName(placeName);
            log.info("place id"+aLong.toString()+"을 삭제했습니다.");
        }
    }

    private boolean isNotReferencedInPlaceLog(Place place) {
        return placeLogRepository.findAllByPlace(place).size() == 0;
    }

}

package com.stella.rememberall.placeLog;

import com.stella.rememberall.placelog.Place;
import com.stella.rememberall.placelog.PlaceRepository;
import com.stella.rememberall.placelog.exception.PlaceLogErrorCode;
import com.stella.rememberall.placelog.exception.PlaceLogException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
public class PlaceRepositoryTest {
    @Autowired
    PlaceRepository placeRepository;

    @Test
    void 관광지저장_유효한요청은_통과한다() {
        Place place = createPlace();
        Place savedPlace = placeRepository.save(place);
        assertEquals(savedPlace.getId(), 126273L);
    }

    @Test
    void 관광지저장_중복이면_수정한다() {
        placeRepository.save(createPlace());

        Place updatedPlace = updatePlaceName("바뀐 이름");
        placeRepository.save(updatedPlace);

        Optional<Place> place = placeRepository.findById(126273L);
        assertTrue(place.isPresent());
        assertEquals(place.get().getName(), "바뀐 이름");
        assertEquals(place.get().getLatitude(), 34.4354594945);
    }

    @Test
    void 여러관광지저장_유효한요청은_통과한다(){
        placeRepository.save(createPlaceWithId(1L));
        placeRepository.save(createPlaceWithId(2L));
        placeRepository.save(createPlaceWithId(3L));

        int size = placeRepository.findAll().size();
        assertEquals(3, size);
    }

    @Test
    void 관광지조회_유효한요청은_통과한다() {
        placeRepository.save(createPlace());
        Optional<Place> place = placeRepository.findById(126273L);
        assertEquals(place.get().getName(), "가계해변");
    }

    @Test
    void 관광지조회_존재하지않는관광지요청은_예외발생한다() {
        Long invalidPlaceId = 126273L;

        PlaceLogException exception = assertThrows(PlaceLogException.class,
                () -> {
                        placeRepository.findById(invalidPlaceId)
                            .orElseThrow(()->new PlaceLogException(PlaceLogErrorCode.NOT_FOUND));
                }
        );
        assertEquals(PlaceLogErrorCode.NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 관광지삭제_유효한요청은_통과한다() {
        Place savedPlace = placeRepository.save(createPlace());
        placeRepository.deleteById(savedPlace.getId());
        Optional<Place> placeNotFound = placeRepository.findById(126273L);
        assertTrue(placeNotFound.isEmpty());
    }

    @Test
    void 관광지삭제_존재하지않는관광지요청은_예외발생한다() {
        Long invalidPlaceId = 126273L;
//        placeRepository.deleteById(invalidPlaceId);

        EmptyResultDataAccessException exception = assertThrows(EmptyResultDataAccessException.class,
                () -> {
                    placeRepository.deleteById(invalidPlaceId);
                }
        );
        Optional<Place> placeNotFound = placeRepository.findById(invalidPlaceId);
        assertTrue(placeNotFound.isEmpty());
    }

    Place createPlace(){
        return Place.builder()
                .id(126273L)
                .name("가계해변")
                .address("전라남도 진도군 고군면 신비의바닷길 47 (고군면)")
                .longitude(126.3547412438)
                .latitude(34.4354594945)
                .build();
    }

    Place createPlaceWithId(Long placeId){
        return Place.builder()
                .id(placeId)
                .name("가계해변")
                .address("전라남도 진도군 고군면 신비의바닷길 47 (고군면)")
                .longitude(126.3547412438)
                .latitude(34.4354594945)
                .build();
    }

    private Place updatePlaceName(String 바뀐_이름) {
        return Place.builder()
                .id(126273L)
                .name(바뀐_이름)
                .address("전라남도 진도군 고군면 신비의바닷길 47 (고군면)")
                .longitude(126.3547412438)
                .latitude(34.4354594945)
                .build();
    }

}
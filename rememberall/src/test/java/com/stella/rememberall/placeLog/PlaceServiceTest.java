package com.stella.rememberall.placeLog;


import com.stella.rememberall.placelog.*;
import com.stella.rememberall.placelog.exception.PlaceLogErrorCode;
import com.stella.rememberall.placelog.exception.PlaceLogException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PlaceServiceTest {

    @Mock
    PlaceRepository placeRepository;
    @Mock
    PlaceLogRepository placeLogRepository;

    PlaceSaveRequestDto createSaveRequestDto(){
        return new PlaceSaveRequestDto(126273L, "가계해변", "전라남도 진도군 고군면 신비의바닷길 47 (고군면)", 126.3547412438, 34.4354594945);
    }

    PlaceSaveRequestDto createUpdateRequestDto(){
        return new PlaceSaveRequestDto(126273L, "수정된 이름", "전라남도 진도군 고군면 신비의바닷길 47 (고군면)", 126.3547412438, 34.4354594945);
    }



    @Nested
    @DisplayName("관광지 등록")
    class CreatePlace {

        private PlaceSaveRequestDto placeSaveRequestDto;

        @BeforeEach
        void setup() {
            placeSaveRequestDto = createSaveRequestDto();
        }

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {
            @Test
            @DisplayName("새로운 관광지 생성")
            void createPlaceSuccess1() {
                Place place = placeSaveRequestDto.toEntity();
                Mockito.when(placeRepository.save(ArgumentMatchers.any(Place.class))).thenReturn(place);

                PlaceService placeService = new PlaceService(placeRepository, placeLogRepository);
                Place result = placeService.saveOrUpdatePlace(placeSaveRequestDto);

                assertEquals(126273L, result.getId());
            }
        }

        @Nested
        @DisplayName("비정상 케이스")
        class FailCase {
            @Test
            @DisplayName("반환된 게시물이 NULL인 경우")
            void createArticleFail1() {
                Mockito.when(placeRepository.save(ArgumentMatchers.any(Place.class))).thenReturn(null);

                PlaceService placeService = new PlaceService(placeRepository, placeLogRepository);
                Place result = placeService.saveOrUpdatePlace(placeSaveRequestDto);

                Assertions.assertThat(result).isNull();
            }
        }
    }

    @Nested
    @DisplayName("관광지 조회")
    class GetPlace {

        private Long placeId;
        private PlaceSaveRequestDto saveDto;

        @BeforeEach
        void setup() {
            this.placeId = 126273L;
            saveDto = createSaveRequestDto();
        }

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {
            @Test
            @DisplayName("관광지 조회")
            void getPlaceSuccess1() {
                // given
                PlaceService placeService = new PlaceService(placeRepository, placeLogRepository);
                Place place = saveDto.toEntity();
                Mockito.when(placeRepository.save(ArgumentMatchers.any(Place.class))).thenReturn(place);
                placeService.saveOrUpdatePlace(saveDto);
                Mockito.when(placeRepository.findById(126273L)).thenReturn(Optional.of(place));
                // when
                Place result = placeService.getPlace(placeId);
                // then
                assertEquals(126273L, result.getId());
            }
        }

        @Nested
        @DisplayName("비정상 케이스")
        class FailCase {
            @Test
            @DisplayName("반환된 게시물이 NULL인 경우")
            void getPlaceFail1() {
                // given
                PlaceService placeService = new PlaceService(placeRepository, placeLogRepository);
                Place place = saveDto.toEntity();
                Mockito.when(placeRepository.save(ArgumentMatchers.any(Place.class))).thenReturn(place);
                placeService.saveOrUpdatePlace(saveDto);
                Mockito.when(placeRepository.findById(126273L)).thenReturn(Optional.empty());
                // when
                PlaceLogException exception = assertThrows(PlaceLogException.class, ()->{
                    placeService.getPlace(placeId);
                });
                // then
                Assertions.assertThat(exception.getErrorCode()).isEqualTo(PlaceLogErrorCode.NOT_FOUND);
            }
        }

    }

}

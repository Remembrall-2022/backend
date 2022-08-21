package com.stella.rememberall.placelog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceSaveRequestDto {
    @NotNull(message = "관광지 고유번호는 빈값일 수 없습니다.") private Long placeId;
    @NotEmpty(message = "관광지 이름은 빈값일 수 없습니다.") private String name;
    private String address;
    private Double longitude;
    private Double latitude;

    public Place toEntity(){
        return Place.builder()
                .id(placeId)
                .name(name)
                .address(address)
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }
}

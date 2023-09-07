package com.stella.rememberall.placelog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceLogUpdateRequestDto {
    private Long placeLogId;
    private Integer placeLogIndex;
    private PlaceSaveRequestDto place;
    private String comment;
}

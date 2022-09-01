package com.stella.rememberall.datelog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class PlaceLogIndexUpdateRequestDto {
    Map<String, Integer> indexAndPlaceLogIds;
}

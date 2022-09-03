package com.stella.rememberall.datelog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PlaceLogIndexUpdateRequestDto {
    Map<String, Integer> indexAndPlaceLogIds;

    public List<Long> getPlaceLogIdList(){
        List<String> placeLogIdStrings = new ArrayList<>(indexAndPlaceLogIds.keySet());
        return placeLogIdStrings.stream()
                .map(s -> Long.parseLong(s))
                .collect(Collectors.toList());
    }
}

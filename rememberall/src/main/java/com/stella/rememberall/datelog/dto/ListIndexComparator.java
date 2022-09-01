package com.stella.rememberall.datelog.dto;

import com.stella.rememberall.placelog.PlaceLogResponseDto;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@NoArgsConstructor
public class ListIndexComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        int placeLogIndex1 = ((PlaceLogResponseDto)o1).getPlaceLogIndex();
        int placeLogIndex2 = ((PlaceLogResponseDto)o2).getPlaceLogIndex();

        if(placeLogIndex1 > placeLogIndex2){
            return 1;
        }else if(placeLogIndex1 < placeLogIndex2){
            return -1;
        }else{
            return 0;
        }

    }
}

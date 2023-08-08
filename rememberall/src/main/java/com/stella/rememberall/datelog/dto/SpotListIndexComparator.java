package com.stella.rememberall.datelog.dto;

import com.stella.rememberall.placelog.PlaceLogResponseDto;
import com.stella.rememberall.placelog.SpotResponseDto;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@NoArgsConstructor
public class SpotListIndexComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        int placeLogIndex1 = ((SpotResponseDto)o1).getIndex();
        int placeLogIndex2 = ((SpotResponseDto)o2).getIndex();

        if(placeLogIndex1 > placeLogIndex2){
            return 1;
        }else if(placeLogIndex1 < placeLogIndex2){
            return -1;
        }else{
            return 0;
        }

    }
}

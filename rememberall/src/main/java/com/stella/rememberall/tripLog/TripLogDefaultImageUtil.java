package com.stella.rememberall.tripLog;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class TripLogDefaultImageUtil {
    private List<String> tripLogDefaultImageList;

    public TripLogDefaultImageUtil(){
        tripLogDefaultImageList = Arrays.asList(
                "https://rememberall-bucket.s3.ap-northeast-2.amazonaws.com/triplog-default-image/%EC%B1%851.png",
                "https://rememberall-bucket.s3.ap-northeast-2.amazonaws.com/triplog-default-image/%EC%B1%852.png",
                "https://rememberall-bucket.s3.ap-northeast-2.amazonaws.com/triplog-default-image/%EC%B1%853.png",
                "https://rememberall-bucket.s3.ap-northeast-2.amazonaws.com/triplog-default-image/%EC%B1%854.png",
                "https://rememberall-bucket.s3.ap-northeast-2.amazonaws.com/triplog-default-image/%EC%B1%855.png",
                "https://rememberall-bucket.s3.ap-northeast-2.amazonaws.com/triplog-default-image/%EC%B1%856.png",
                "https://rememberall-bucket.s3.ap-northeast-2.amazonaws.com/triplog-default-image/%EC%B1%857.png"
        );
    }

    public String getImgUrl(int index){
        return tripLogDefaultImageList.get(index);
    }

    public int getImgListSize(){
        return tripLogDefaultImageList.size();
    }
}

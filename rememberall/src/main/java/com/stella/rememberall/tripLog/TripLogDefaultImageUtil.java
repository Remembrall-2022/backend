package com.stella.rememberall.tripLog;

import com.stella.rememberall.userLogImg.S3Util;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class TripLogDefaultImageUtil {
    private final S3Util s3Util;
    private List<String> tripLogDefaultImageList;

    public TripLogDefaultImageUtil(S3Util s3Util){
        this.s3Util = s3Util;
        tripLogDefaultImageList = Arrays.asList(
                "triplog-default-image/책1.png",
                "triplog-default-image/책2.png",
                "triplog-default-image/책3.png",
                "triplog-default-image/책4.png",
                "triplog-default-image/책5.png",
                "triplog-default-image/책6.png",
                "triplog-default-image/책7.png",
                "triplog-default-image/책8.png",
                "triplog-default-image/책9.png",
                "triplog-default-image/책10.png",
                "triplog-default-image/책11.png"
        );
    }

    public String getImgUrl(int index){
        return s3Util.getUrl(tripLogDefaultImageList.get(index));
//        return tripLogDefaultImageList.get(index);
    }

    public int getImgListSize(){
        return tripLogDefaultImageList.size();
    }
}

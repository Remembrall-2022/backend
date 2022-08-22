package com.stella.rememberall.userLogImg;

import com.stella.rememberall.placelog.PlaceLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserLogImgRepository extends JpaRepository<UserLogImg, Long> {
    List<UserLogImg> findAllByPlaceLog(PlaceLog placeLog);
}

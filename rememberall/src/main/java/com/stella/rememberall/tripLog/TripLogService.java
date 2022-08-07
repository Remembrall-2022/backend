package com.stella.rememberall.tripLog;

import com.stella.rememberall.domain.AuthType;
import com.stella.rememberall.user.User;
import com.stella.rememberall.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TripLogService {

    private final TripLogRepository tripLogRepository;
    private final UserRepository userRepository;

    /* TODO
    일기장 create
      a. 일기장 표지는 버킷에서 불러올건지? oo
      b. 일기장 생성 날짜 잘 들어가는지?
     */

    @Transactional
    public void saveTripLog(TripLogSaveRequestDto dto){
        //User user = new User(AuthType.EMAIL, "email", "pw", "name"); // TODO : 회원가입 개발 후 토큰에서 유저 조회하는 로직으로 변경해야함
        tripLogRepository.save(dto.toEntity());
    }

    @Transactional
    public TripLog getTripLog(Long id){
        return tripLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("no such data"));
    }




}

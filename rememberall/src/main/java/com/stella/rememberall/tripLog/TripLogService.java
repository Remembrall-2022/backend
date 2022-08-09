package com.stella.rememberall.tripLog;

import com.stella.rememberall.tripLog.dto.TripLogResponseDto;
import com.stella.rememberall.tripLog.dto.TripLogSaveRequestDto;
import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

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
    public TripLogResponseDto saveTripLog(TripLogSaveRequestDto dto){
        // User user = new User(AuthType.EMAIL, "email", "pw", "name"); // TODO : 회원가입 개발 후 토큰에서 유저 조회하는 로직으로 변경해야함
        TripLog savedTripLog = tripLogRepository.save(dto.toEntity()); // TODO : 레포지토리 저장 실패 예외 처리
        // TODO : 디폴트 이미지 지정 방법 논의 후 이미지 setter
        return TripLogResponseDto.of(savedTripLog);
    }

    @Transactional
    public TripLogResponseDto findTripLog(Long id){
        TripLog foundTripLog = tripLogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("no such data"));
        return TripLogResponseDto.of(foundTripLog);
    }

    @Transactional
    public List<TripLogResponseDto> findTripLogList(User user){
        List<TripLog> entityList = tripLogRepository.findAll();
        //List<TripLog> entityList = tripLogRepository.findAllByUser(user);
        return mapEntityListToDtoList(entityList);
    }

    private List<TripLogResponseDto> mapEntityListToDtoList(List<TripLog> foundTripLogList) {
        return foundTripLogList.stream().map(TripLogResponseDto::of).collect(Collectors.toList());
    }


}

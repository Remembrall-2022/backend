package com.stella.rememberall.datelog;

import com.stella.rememberall.datelog.domain.DateLog;
import com.stella.rememberall.datelog.dto.DateLogSaveRequestDto;
import com.stella.rememberall.datelog.exception.DateLogException;
import com.stella.rememberall.datelog.repository.DateLogRepository;
import com.stella.rememberall.tripLog.TripLog;
import com.stella.rememberall.tripLog.TripLogRepository;
import com.stella.rememberall.tripLog.exception.TripLogException;
import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.user.dto.EmailUserSaveRequestDto;
import com.stella.rememberall.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DateLogServiceTest {

    @Autowired UserRepository userRepository;
    @Autowired TripLogRepository tripLogRepository;
    @Autowired DateLogService dateLogService;
    @Autowired
    DateLogRepository dateLogRepository;

    @Test
    void createDateLog() {
        // 유저 생성 후 저장
        String email = "stella@gmail.com";
        String password = "stellapw";
        String name = "stella1";
        EmailUserSaveRequestDto dto = new EmailUserSaveRequestDto(email, password, name);
        User emailUser = dto.toEntity();
        userRepository.save(emailUser);

        // 유저 사용해서 일기장 생성
        TripLog tripLog = TripLog.builder()
                .title("Seoul Trip")
                .user(emailUser)
                .tripStartDate(LocalDate.now())
                .tripEndDate(LocalDate.now().plusWeeks(1))
                .build();
        tripLogRepository.save(tripLog);

        DateLogSaveRequestDto dateLogDto = DateLogSaveRequestDto.builder()
                .date(LocalDate.now())
                .answer("this is answer without question")
                .weather("맑음")
                .degree(30)
                .build();
        dateLogService.createDateLog(tripLog.getId(), dateLogDto);
        DateLog found = dateLogRepository.findByTripLogAndDate(tripLog, LocalDate.now()).get(0);

        Assertions.assertThat(found.getId()).isEqualTo(1L);

        //기존에 존재하는 dateLog와 같은 일기장, 같은 날짜를 가지는 dateLog를 생성하면 예외

        DateLogSaveRequestDto dateLogDto2 = DateLogSaveRequestDto.builder().
                date(LocalDate.now())
                .build();
        assertThrows(DateLogException.class, ()-> dateLogService.createDateLog(tripLog.getId(), dateLogDto2));


        // 존재하지 않는 일기장 id를 받아서 일기 생성하면 예외
        DateLogSaveRequestDto dateLogDto3 = DateLogSaveRequestDto.builder().
                date(LocalDate.of(2022, 01,22))
                .build();
        assertThrows(TripLogException.class, ()-> dateLogService.createDateLog(10L, dateLogDto3));

    }


}

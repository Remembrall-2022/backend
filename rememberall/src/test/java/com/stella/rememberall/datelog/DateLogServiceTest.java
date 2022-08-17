package com.stella.rememberall.datelog;

import com.stella.rememberall.tripLog.TripLog;
import com.stella.rememberall.tripLog.TripLogRepository;
import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.user.dto.EmailUserSaveRequestDto;
import com.stella.rememberall.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class DateLogServiceTest {

    @Autowired UserRepository userRepository;
    @Autowired TripLogRepository tripLogRepository;
    @Autowired DateLogService dateLogService;
    @Autowired DateLogRepository dateLogRepository;

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

        DateLogSaveRequestDto dateLogDto = DateLogSaveRequestDto.builder(tripLog, LocalDate.now())
                .answer("this is answer without question")
                .build();
        dateLogService.createDateLog(dateLogDto);
        DateLog found = dateLogRepository.findByTripLogAndDate(tripLog, LocalDate.now()).get(0);

        Assertions.assertThat(found.getId()).isEqualTo(1L);

        //기존에 존재하는 dateLog와 같은 일기장, 같은 날짜를 가지는 dateLog를 생성하면 예외

        DateLogSaveRequestDto dateLogDto2 = DateLogSaveRequestDto.builder(tripLog, LocalDate.now()).build();
        assertThrows(IllegalStateException.class, ()-> dateLogService.createDateLog(dateLogDto2));

    }


}

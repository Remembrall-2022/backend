package com.stella.rememberall.tripLog;

import com.stella.rememberall.user.EmailUserSaveRequestDto;
import com.stella.rememberall.user.User;
import com.stella.rememberall.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TripLogServiceTest {

    @Autowired TripLogRepository tripLogRepository;
    @Autowired UserRepository userRepository;

    String title = "title";
    LocalDate tripStartDate = LocalDate.of(2021, 1, 1);
    LocalDate tripEndDate = LocalDate.of(2021, 1, 3);

    @Test
    void saveTripLog(){
        // given
        TripLog tripLog = createTripLog();
        // when
        tripLogRepository.save(tripLog);
        // then
        TripLog savedTripLog = tripLogRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("no such data"));
        assertNotNull(savedTripLog);
    }

    User createEmailUser(){
        String email = "email..@com";
        String password = "pw";
        String name = "name1";

        EmailUserSaveRequestDto dto = new EmailUserSaveRequestDto(email, password, name);
        User emailUser = dto.toEntity();
        return emailUser;
    }

    TripLog createTripLog(){
        String title = "title";
        LocalDate tripStartDate = LocalDate.of(2021, 1, 1);
        LocalDate tripEndDate = LocalDate.of(2021, 1, 3);

        TripLogSaveRequestDto dto = new TripLogSaveRequestDto(title, tripStartDate, tripEndDate);
        TripLog tripLog = dto.toEntity();
        return tripLog;
    }

}
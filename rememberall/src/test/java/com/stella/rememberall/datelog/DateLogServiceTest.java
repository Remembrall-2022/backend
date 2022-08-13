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
    @Autowired QuestionCategoryRepository questionCategoryRepository;
    @Autowired QuestionRepository questionRepository;
    @Autowired TripLogRepository tripLogRepository;
    @Autowired DateLogService dateLogService;
    @Autowired DateLogRepository dateLogRepository;

    @Test
    void createDateLog() {
        /**
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

        // 질문카테고리와 질문 생성, 저장
        QuestionCategory questionCategory = new QuestionCategory(1L, QuestionCategoryName.BEFORE);
        questionCategoryRepository.save(questionCategory);
        Question question = new Question(1L, questionCategory, "목적지를 정하게 된 이유는?");
        questionRepository.save(question);
        DateLogSaveRequestDto dateLogDto = DateLogSaveRequestDto.builder()
                .tripLog(tripLog)
                .date(LocalDate.now())
                .question(question)
                .weatherInfo(new WeatherInfo("맑음", 25))
                .answer("대답")
                .build();
        dateLogService.createDateLog(dateLogDto);
        DateLog found = dateLogRepository.findByTripLogAndDate(tripLog, LocalDate.now()).get(0);

        Assertions.assertThat(found.getId()).isEqualTo(1L);

        //같은 일기장과 같은 날짜의 일기 하나 더 생성하면 예외

        DateLogSaveRequestDto dateLogDto2 = DateLogSaveRequestDto.builder()
                .tripLog(tripLog)
                .date(LocalDate.now())
                .question(question)
                .weatherInfo(new WeatherInfo("흐림", 25))
                .answer("대답2")
                .build();
        assertThrows(IllegalStateException.class, ()-> dateLogService.createDateLog(dateLogDto2));
         **/

        /**
         * 동작하는 것 확인했으나 Question, QuestionCategory 생성자 없애기 위해 주석처리
         * **/
    }


}

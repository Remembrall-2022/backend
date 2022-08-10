package com.stella.rememberall.tripLog;

import com.stella.rememberall.tripLog.dto.TripLogSaveRequestDto;
import com.stella.rememberall.tripLog.dto.TripLogUpdateRequestDto;
import com.stella.rememberall.tripLog.exception.TripLogException;
import com.stella.rememberall.user.dto.EmailUserSaveRequestDto;
import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.tripLog.exception.TripLogErrorCode;
import com.stella.rememberall.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
class TripLogTest {

    @Autowired TripLogRepository tripLogRepository;
    @Autowired UserRepository userRepository;

    String title = "title";
    LocalDate tripStartDate = LocalDate.of(2021, 1, 1);
    LocalDate tripEndDate = LocalDate.of(2021, 1, 3);

    String email = "email..@com";
    String password = "pw";
    String name = "name1";

    @Test
    void 일기장생성_유효한요청은_통과한다(){
        // given
        User user = createAndSaveEmailUser();
        TripLog tripLogToSave = createTripLog(user);
        // when
        TripLog savedTripLog = tripLogRepository.save(tripLogToSave);
        // then
        assertNotNull(savedTripLog);
    }

    // TODO : Contoller test - 일기장 title이 빈 값일때
    // exception : MethodArgumentNotValidException
    // error name : NotEmpty
    // error message : 일기장의 title은 빈값일 수 없습니다.
    @Test
    void 일기장생성_title이빈값이면_예외발생한다(){
    }


    @Test
    void 일기장id조회_유효한id는_통과한다(){
        // given
        User user = createAndSaveEmailUser();
        TripLog tripLogToSave = createTripLog(user);
        TripLog savedTripLog = tripLogRepository.save(tripLogToSave);
        // when
        TripLog foundTripLog = tripLogRepository.findById(savedTripLog.getId())
                .orElseThrow(() -> new TripLogException(TripLogErrorCode.TRIPLOG_NOT_FOUND));
        // then
        assertNotNull(foundTripLog);
    }

    @Test
    void 일기장id조회_존재하지않는id는_예외발생한다(){
        // given
        Long invalidTripLogPk = 10L;
        // when
        TripLogException exception = assertThrows(TripLogException.class,
                () -> {
                        tripLogRepository.findById(invalidTripLogPk)
                                .orElseThrow(() -> new TripLogException(TripLogErrorCode.TRIPLOG_NOT_FOUND));
                }
        );
        // then
        assertEquals(TripLogErrorCode.TRIPLOG_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 일기장회원별조회_유효한user는_통과한다(){
        // given
        User user = createAndSaveEmailUser();
        tripLogRepository.save(createTripLog(user));
        tripLogRepository.save(createTripLog(user));
        tripLogRepository.save(createTripLog(user));
        // when
        List<TripLog> allByUser = tripLogRepository.findAllByUser(user);
        // then
        assertEquals(3, allByUser.size());
    }

    @Test
    void 일기장회원별조회_유효한user이고_생성한일기장이없을때_통과한다(){
        // given
        User user = createAndSaveEmailUser();
        // when
        List<TripLog> allByUser = tripLogRepository.findAllByUser(user);
        // then
        assertEquals(0, allByUser.size());
    }

    @Test
    void 일기장수정_유효한user는_통과한다(){
        // given
        User user = createAndSaveEmailUser();
        TripLog tripLogToSave = createTripLog(user);
        TripLog savedTripLog = tripLogRepository.save(tripLogToSave);
        // when
        TripLogUpdateRequestDto dto = new TripLogUpdateRequestDto(savedTripLog.getId(), "수정된 title", LocalDate.of(2022, 5, 3), LocalDate.of(2022, 5, 5));
        TripLog updatedTripLog = tripLogRepository.save(dto.toEntityWithUserAndId(user, savedTripLog.getId()));
        // then
        assertEquals("수정된 title", updatedTripLog.getTitle());
    }

    @Test
    void 일기장수정_권한없는user는_예외발생한다(){
        // given
        User realUser = createAndSaveEmailUser();
        User fakeUser = new EmailUserSaveRequestDto("fakeemail", password, name).toEntity();
        TripLog savedTripLog = tripLogRepository.save(createTripLog(realUser));
        // when
        TripLogException exception = assertThrows(TripLogException.class,
                () -> {
                    if (savedTripLog.getUser().getId() != fakeUser.getId()) {
                        throw new TripLogException(TripLogErrorCode.NO_AUTHORITY_TO_UPDATE);
                    }
                });
        // then
        assertEquals(TripLogErrorCode.NO_AUTHORITY_TO_UPDATE, exception.getErrorCode());

    }

    // TODO : Contoller test - 일기장 title이 빈 값일때
    // exception : MethodArgumentNotValidException
    // error name : NotEmpty
    // error message : 일기장의 title은 빈값일 수 없습니다.
    @Test
    void 일기장수정_title이빈값이면_예외발생한다(){

    }


    User createAndSaveEmailUser(){
        EmailUserSaveRequestDto dto = new EmailUserSaveRequestDto(email, password, name);
        User emailUser = dto.toEntity();
        return userRepository.save(emailUser);
    }

    TripLog createTripLog(User user){
        TripLogSaveRequestDto dto = new TripLogSaveRequestDto(title, tripStartDate, tripEndDate);
        TripLog tripLog = dto.toEntityWithUser(user);
        return tripLog;
    }

    TripLog createTripLogWithoutTitle(User user) {
        TripLogSaveRequestDto dto = new TripLogSaveRequestDto("", tripStartDate, tripEndDate);
        TripLog tripLog = dto.toEntityWithUser(user);
        return tripLog;
    }

}

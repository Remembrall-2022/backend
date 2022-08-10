package com.stella.rememberall.tripLog.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.stella.rememberall.tripLog.TripLog;
import com.stella.rememberall.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class TripLogSaveRequestDto {
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate tripStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate tripEndDate;

    public TripLog toEntity() {
        return TripLog.builder()
                .title(title)
                .tripStartDate(tripStartDate)
                .tripEndDate(tripEndDate)
                .build();
    }

    public TripLog toEntityWithUser(User user) {
        return TripLog.builder()
                .title(title)
                .tripStartDate(tripStartDate)
                .tripEndDate(tripEndDate)
                .user(user)
                .build();
    }
}

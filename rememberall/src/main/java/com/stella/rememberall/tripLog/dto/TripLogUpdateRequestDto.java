package com.stella.rememberall.tripLog.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.stella.rememberall.tripLog.TripLog;
import com.stella.rememberall.tripLog.validation.StartAndEndDateCheck;
import com.stella.rememberall.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@StartAndEndDateCheck(startDate = "tripStartDate", endDate = "tripEndDate")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class TripLogUpdateRequestDto {

    @Setter private Long tripLogId;

    @NotEmpty(message = "일기장의 title은 빈값일 수 없습니다.")
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate tripStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate tripEndDate;

    public TripLog toEntityWithUserAndId(User user, Long tripLogId) {
        return TripLog.builder()
                .id(tripLogId)
                .title(title)
                .tripStartDate(tripStartDate)
                .tripEndDate(tripEndDate)
                .user(user)
                .build();
    }
}

package com.stella.rememberall.datelog.dto;

import com.stella.rememberall.datelog.domain.DateLog;
import com.stella.rememberall.datelog.domain.Question;
import com.stella.rememberall.datelog.domain.WeatherInfo;
import com.stella.rememberall.placelog.PlaceLog;
import com.stella.rememberall.placelog.PlaceLogResponseDto;
import com.stella.rememberall.placelog.SpotResponseDto;
import com.stella.rememberall.userLogImg.UserLogImg;
import com.stella.rememberall.userLogImg.UserLogImgResponseDto;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DateLogResponseDto {

    private LocalDate date;
    private WeatherInfo weatherInfo;
    private QuestionVo question;
    private String answer;
    @Setter private List<PlaceLogResponseDto> placeLogList;

    @Setter private List<SpotResponseDto> constellationMapFromDateLog;

    public static DateLogResponseDto of(DateLog dateLog){
        Question question = Optional.ofNullable(dateLog.getQuestion()).orElse(null);
        String answer = Optional.ofNullable(dateLog.getAnswer()).orElse("");


//
//        List<PlaceLog> placeLogList = dateLog.getPlaceLogList().stream().sorted(Comparator.comparing(PlaceLog::getIndex))
//                .collect(Collectors.toList());
//        List<PlaceLogResponseDto> placeLogResponseDtoList = new ArrayList<>();
//        for(PlaceLog placeLog:placeLogList){
//            List<UserLogImg> userLogImgList = placeLog.getUserLogImgList();
//            List<UserLogImgResponseDto> userLogImgResponseDtos = new ArrayList<>();
//            for(UserLogImg userLogImg:userLogImgList){
//                userLogImgResponseDtos.add(UserLogImgResponseDto.of(userLogImg.getIndex(), userLogImg.getFileKey()));
//            }
//
//            PlaceLogResponseDto placeLogResponseDto = PlaceLogResponseDto.of(placeLog);
//            placeLogResponseDto.updateUserLogImgListWithImgUrl(userLogImgList);
//            placeLogResponseDtoList.add(placeLogResponseDto);
//        }

        return DateLogResponseDto.builder()
                .date(dateLog.getDate())
                .weatherInfo(dateLog.getWeatherInfo())
                .question(QuestionVo.of(question.getId(), question.getTopic()))
                .answer(answer)
//                .placeLogList(placeLogResponseDtoList)
                .build();
    }

    @Builder
    public DateLogResponseDto(LocalDate date, WeatherInfo weatherInfo, QuestionVo question, String answer) {
        this.date = date;
        this.weatherInfo = weatherInfo;
        this.question = question;
        this.answer = answer;
    }
}

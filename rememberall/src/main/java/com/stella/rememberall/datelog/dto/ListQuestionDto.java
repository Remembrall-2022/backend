package com.stella.rememberall.datelog.dto;

import com.stella.rememberall.datelog.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListQuestionDto {

    private List<Question> questionList;
}

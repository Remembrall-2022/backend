package com.stella.rememberall.datelog.dto;

import com.stella.rememberall.datelog.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionVo {

    private Long id;
    private String category;
    private String questionName;

    public static QuestionVo of(Question question) {
        if (question != null)
            return new QuestionVo(question.getId(), question.getQuestionCategory().getKor(), question.getTopic());
        else
            return null;
    }
}

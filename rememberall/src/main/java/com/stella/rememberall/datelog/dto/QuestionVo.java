package com.stella.rememberall.datelog.dto;

import com.stella.rememberall.datelog.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class QuestionVo {

    private Long id;
    private String questionName;

    public static QuestionVo of(Question question) {
        if (question != null)
            return new QuestionVo(question.getId(), question.getTopic());
        else
            return null;
    }
}

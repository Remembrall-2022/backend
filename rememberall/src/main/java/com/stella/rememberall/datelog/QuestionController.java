package com.stella.rememberall.datelog;

import com.stella.rememberall.datelog.domain.Question;
import com.stella.rememberall.datelog.domain.QuestionCategoryName;
import com.stella.rememberall.datelog.dto.ListQuestionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/question/random")
    public Question readRandomQuestion() {
        return questionService.readRandomQuestion();
    }

}

package com.stella.rememberall.datelog;

import com.stella.rememberall.datelog.dto.QuestionVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/question/random")
    public QuestionVo readRandomQuestion() {
        return questionService.readRandomQuestion();
    }

}

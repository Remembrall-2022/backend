package com.stella.rememberall.datelog;

import com.stella.rememberall.datelog.domain.Question;
import com.stella.rememberall.datelog.domain.QuestionCategory;
import com.stella.rememberall.datelog.domain.QuestionCategoryName;
import com.stella.rememberall.datelog.dto.ListQuestionDto;
import com.stella.rememberall.datelog.exception.QuestionExCode;
import com.stella.rememberall.datelog.exception.QuestionException;
import com.stella.rememberall.datelog.repository.QuestionCategoryRepository;
import com.stella.rememberall.datelog.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionCategoryRepository questionCategoryRepository;

    @Transactional
    public Question readRandomQuestion() {
        Long count = questionRepository.countQuestions();
        Long randomId = abs(new Random().nextLong()%count)+1;

        return questionRepository.findById(randomId)
                .orElseThrow(() -> new QuestionException(QuestionExCode.QUESTION_NOT_FOUND));
    }

    /**
    @Transactional
    public ListQuestionDto readQuestionByCategory(QuestionCategoryName questionCategoryName) {
        QuestionCategory category = questionCategoryRepository.findByQuestionCategoryName(questionCategoryName)
                .orElseThrow(() -> new QuestionException(QuestionExCode.QUESTION_NOT_FOUND));
        List<Question> questionList = questionRepository.findByQuestionCategory(category);
        return new ListQuestionDto(questionList);
    }
    */
}

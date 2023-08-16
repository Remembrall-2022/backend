package com.stella.rememberall.datelog;

import com.stella.rememberall.datelog.domain.Question;
import com.stella.rememberall.datelog.dto.QuestionVo;
import com.stella.rememberall.datelog.exception.QuestionExCode;
import com.stella.rememberall.datelog.exception.QuestionException;
import com.stella.rememberall.datelog.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public QuestionVo readRandomQuestion() {
        Long count = questionRepository.countQuestions();
        if (count==0) throw new QuestionException((QuestionExCode.QUESTION_NOT_EXISTED));

        Long randomId = abs(new Random().nextLong()%count)+1;
        Question question = questionRepository.findById(randomId)
                .orElseThrow(() -> new QuestionException(QuestionExCode.QUESTION_NOT_FOUND));

        return QuestionVo.of(question);
    }

    @Transactional
    public List<QuestionVo> readAllQuestions() {
        List<QuestionVo> result = new ArrayList<>();
        questionRepository.findAll().stream()
                .forEach(q -> result.add(QuestionVo.of(q)));

        return result;
    }

}

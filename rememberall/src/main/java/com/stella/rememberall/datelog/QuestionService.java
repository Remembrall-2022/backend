package com.stella.rememberall.datelog;

import com.stella.rememberall.datelog.domain.Question;
import com.stella.rememberall.datelog.exception.QuestionExCode;
import com.stella.rememberall.datelog.exception.QuestionException;
import com.stella.rememberall.datelog.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static java.lang.Math.abs;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public Question readRandomQuestion() {
        Long count = questionRepository.countQuestions();
        Long randomId = abs(new Random().nextLong()%count)+1;

        log.info("Count - {}// QuestionId - {}", count, randomId);

        return questionRepository.findById(randomId)
                .orElseThrow(() -> new QuestionException(QuestionExCode.QUESTION_NOT_FOUND));
    }
}

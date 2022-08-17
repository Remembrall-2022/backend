package com.stella.rememberall.datelog.repository;

import com.stella.rememberall.datelog.domain.Question;
import com.stella.rememberall.datelog.domain.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByQuestionCategory(QuestionCategory category);
}

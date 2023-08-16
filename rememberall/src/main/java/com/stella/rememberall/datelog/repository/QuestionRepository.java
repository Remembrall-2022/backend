package com.stella.rememberall.datelog.repository;

import com.stella.rememberall.datelog.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT count(q.id) FROM Question q")
    Long countQuestions();
}

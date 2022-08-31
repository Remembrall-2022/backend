package com.stella.rememberall.datelog.repository;

import com.stella.rememberall.datelog.domain.QuestionCategory;
import com.stella.rememberall.datelog.domain.QuestionCategoryName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {

    @Query("SELECT c FROM QuestionCategory c WHERE c.questionCategoryName = :name")
    Optional<QuestionCategory> findCategoryByName(QuestionCategoryName name);
}

package com.stella.rememberall.datelog.repository;

import com.stella.rememberall.datelog.domain.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {
}

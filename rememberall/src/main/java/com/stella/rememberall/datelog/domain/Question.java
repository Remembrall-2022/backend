package com.stella.rememberall.datelog.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "question")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
public class Question {
    @Id
    @Column(name = "question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="question_category_id")
    private QuestionCategory questionCategory;

    @Column(name = "topic")
    private String topic;

}

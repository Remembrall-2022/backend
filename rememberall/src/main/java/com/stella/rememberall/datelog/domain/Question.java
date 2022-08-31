package com.stella.rememberall.datelog.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "question")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Question {
    @Id
    @Column(name = "question_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="question_category_id")
    private QuestionCategory questionCategory;

    @Column(name = "topic")
    private String topic;

}

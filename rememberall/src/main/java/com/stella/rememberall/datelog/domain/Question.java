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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne  TODO: 없애기
    @Enumerated(EnumType.STRING)
    @JoinColumn(name="question_category")
    private QuestionCategoryName questionCategory;

    @Column(name = "topic")
    private String topic;

    public Question(QuestionCategoryName questionCategory, String topic) {
        this.questionCategory = questionCategory;
        this.topic = topic;
    }
}

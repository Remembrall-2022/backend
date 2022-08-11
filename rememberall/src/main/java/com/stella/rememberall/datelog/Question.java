package com.stella.rememberall.datelog;

import javax.persistence.*;

@Table(name = "question")
@Entity
public class Question {
    @Id
    @Column(name = "question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="question_category_id")
    private QuestionCategory questionCategory;

}

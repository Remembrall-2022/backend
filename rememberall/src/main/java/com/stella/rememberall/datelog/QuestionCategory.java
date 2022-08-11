package com.stella.rememberall.datelog;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "question_category")
@Entity
public class QuestionCategory {
    @Id
    @Column(name = "question_category_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_category_name")
    private QuestionCategoryName questionCategoryName;

    @OneToMany(mappedBy = "questionCategory")
    private List<Question> questionList = new ArrayList<>();


}

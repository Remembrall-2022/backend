package com.stella.rememberall.question;

import com.stella.rememberall.datelog.domain.Question;
import com.stella.rememberall.datelog.domain.QuestionCategoryName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class QuestionItemProcessor implements ItemProcessor<QuestionItem, Question> {

    @Override
    public Question process(final QuestionItem item) throws Exception {
        final String topic = item.getTopic();
        final QuestionCategoryName category = convertStringToCategory(item.getQuestionCategory());
        return new Question(category, topic);
    }

    private QuestionCategoryName convertStringToCategory(String stringCategory) {
        // TODO: QuestionCategory 없애버리기
        return QuestionCategoryName.of(stringCategory);
    }
}

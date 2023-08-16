package com.stella.rememberall.question;

import com.stella.rememberall.datelog.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public FlatFileItemReader<QuestionItem> reader() {
        return new FlatFileItemReaderBuilder<QuestionItem>()
                .name("questionItemReader")
                .resource(new ClassPathResource("questions.csv"))
                .delimited()
                .names("topic", "questionCategory")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(QuestionItem.class);
                }})
                .build();
    }

    @Bean
    public QuestionItemProcessor processor() {
        return new QuestionItemProcessor();
    }
}

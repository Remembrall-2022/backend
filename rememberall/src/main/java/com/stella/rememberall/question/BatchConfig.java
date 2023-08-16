package com.stella.rememberall.question;

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
    public FlatFileItemReader<QuestionData> reader() {
        return new FlatFileItemReaderBuilder<QuestionData>()
                .name("questionDataReader")
                .resource(new ClassPathResource("questions.csv"))
                .delimited()
                .names("topic", "questionCategory")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(QuestionData.class);
                }})
                .build();
    }
}

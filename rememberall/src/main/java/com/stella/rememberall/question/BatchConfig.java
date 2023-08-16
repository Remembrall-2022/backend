package com.stella.rememberall.question;

import com.stella.rememberall.datelog.domain.Question;
import com.stella.rememberall.datelog.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final QuestionRepository questionRepository;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;

    @Bean
    public FlatFileItemReader<QuestionItem> reader() {
        log.info("[Batch reader] Batch process writer.");
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
        log.info("[Batch processor] Batch process writer.");
        return new QuestionItemProcessor();
    }

    @Bean
    public ItemWriter<Question> writer() {
        return categories -> {
            log.info("[Batch writer] Batch process writer.");
            questionRepository.saveAll(categories);
        };
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<QuestionItem, Question>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job job1() {
        return jobBuilderFactory.get("job1")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

}

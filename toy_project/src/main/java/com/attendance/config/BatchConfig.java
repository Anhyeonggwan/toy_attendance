package com.attendance.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.attendance.dao.MemberDao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * extends DefaultBatchConfiguration 또는 EnableBatchProcessing 어노테이션이 있으면 자동실행이 되지 않는다.
 * application.properties 파일의 spring.batch.job.name이 JOB_NAME과 같아야 한다.
 * 
 * */

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfig extends DefaultBatchConfiguration{
	
	private final String JOB_NAME = "testJob";
	private final String STEP_NAME = "testStep";
	
	private final MemberDao memberDao;
	
	/**
     * Job 등록
     */
    @Bean
    public Job testJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer()) // sequential id
                .start(testStep(jobRepository, transactionManager)) // step 설정
                .build();
    }
    
    /**
     * Step 등록
     */
    @Bean
    @JobScope
    public Step testStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .tasklet(testTasklet(), transactionManager) // tasklet 설정
                .build();
    }
    
    /**
     * Tasklet: Reader-Processor-Writer를 구분하지 않는 단일 step
     */
    @Bean
    @StepScope
    public Tasklet testTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                log.info("Spring Batch Test Success");
                return RepeatStatus.FINISHED; // 작업에 대한 Status 명시
            }
        };
    }

}


































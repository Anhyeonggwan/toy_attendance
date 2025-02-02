package com.attendance.batch;

import java.util.Collections;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BatchScheduler {
	
	private final Job testJob;
	private final JobLauncher jobLauncher;
	
	@Scheduled(cron = "0/5 * * * * ?") // 5초마다 실행할 수 있게 함
    public void testJobRun() throws JobInstanceAlreadyCompleteException
    , JobExecutionAlreadyRunningException
    , JobParametersInvalidException
    , JobRestartException {

        JobParameters jobParameters = new JobParameters(
                Collections.singletonMap("requestTime", new JobParameter(System.currentTimeMillis(), Long.class))
        );

        jobLauncher.run(testJob, jobParameters);
    }

}

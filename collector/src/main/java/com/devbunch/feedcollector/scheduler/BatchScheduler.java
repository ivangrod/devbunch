package com.devbunch.feedcollector.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BatchScheduler {

	@Autowired
	private SimpleJobLauncher jobLauncher;

	@Autowired
	private Job job;

	@Scheduled(cron = "${feed-collector.cron}")
	public void perform() throws JobExecutionException {

		log.info("Job [{}] Started", job.getName());

		final JobParameters param = new JobParametersBuilder().addLong("JobID", System.nanoTime()).toJobParameters();

		final JobExecution execution = jobLauncher.run(job, param);

		log.info("Job [{}] finished with status : {}", job.getName(), execution.getStatus());
	}

}

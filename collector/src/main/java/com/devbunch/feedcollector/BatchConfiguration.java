package com.devbunch.feedcollector;

import java.util.Map.Entry;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import com.devbunch.feedcollector.processor.FeedItemProcessor;
import com.devbunch.feedcollector.reader.FeedItemReader;
import com.devbunch.feedcollector.writer.FeedItemSender;
import com.devbunch.model.FeedItem;
import com.rometools.rome.feed.synd.SyndEntry;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	// tag::jobstep[]
	@Bean
	public Job collectFeed(@Qualifier("step1") Step step1) {
		return jobBuilderFactory.get("collectFeed").start(step1).build();
	}

	@Bean
	public Step step1(FeedItemReader reader, FeedItemProcessor feedItemProcessor, FeedItemSender sender) {
		return stepBuilderFactory.get("step1").allowStartIfComplete(true).<Entry<String, SyndEntry>, FeedItem>chunk(10)
				.reader(reader).processor(feedItemProcessor).writer(sender).build();
	}
	// end::jobstep[]

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new ResourcelessTransactionManager();
	}

	@Bean
	public FactoryBean<JobRepository> mapJobRepositoryFactory(PlatformTransactionManager txManager)
			throws Exception {
		MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean(txManager);
		factory.afterPropertiesSet();
		return factory;
	}

	@Bean
	public JobRepository jobRepository(FactoryBean<JobRepository> factory) throws Exception {
		return factory.getObject();
	}

	@Bean
	public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
		final SimpleJobLauncher launcher = new SimpleJobLauncher();
		launcher.setJobRepository(jobRepository);
		return launcher;
	}

}

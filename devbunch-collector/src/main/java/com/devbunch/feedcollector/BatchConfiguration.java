package com.devbunch.feedcollector;

import java.util.Map.Entry;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.devbunch.feedcollector.processor.FeedItemProcessor;
import com.devbunch.feedcollector.reader.FeedItemReader;
import com.devbunch.feedcollector.writer.FeedItemSender;
import com.devbunch.model.FeedItem;
import com.rometools.rome.feed.synd.SyndEntry;

@Configuration
@EnableBatchProcessing
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
  public Step step1(FeedItemReader reader, FeedItemProcessor feedItemProcessor,
      FeedItemSender sender) {
    return stepBuilderFactory.get("step1").allowStartIfComplete(true)
        .<Entry<String, SyndEntry>, FeedItem>chunk(10).reader(reader).processor(feedItemProcessor)
        .writer(sender).build();
  }
  // end::jobstep[]
}

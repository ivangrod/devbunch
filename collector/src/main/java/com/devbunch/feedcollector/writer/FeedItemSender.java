package com.devbunch.feedcollector.writer;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import com.devbunch.model.FeedItem;

@Component
public class FeedItemSender implements ItemWriter<FeedItem> {

  private static final Logger logger = LoggerFactory.getLogger(FeedItemSender.class);

  @Value("${feed-collector.sender.topicName}")
  private String topicName;

  @Autowired
  private KafkaTemplate<String, FeedItem> kafkaTemplate;

  @Override
  public void write(List<? extends FeedItem> feedItems) throws Exception {
    feedItems.forEach(feedItem -> {
      ListenableFuture<SendResult<String, FeedItem>> sendResultFuture =
          kafkaTemplate.send(topicName, feedItem);
      sendResultFuture.addCallback(new ListenableFutureCallback<SendResult<String, FeedItem>>() {

        @Override
        public void onSuccess(SendResult<String, FeedItem> result) {
          logger.info(result.getProducerRecord().value().getUri());
        }

        @Override
        public void onFailure(Throwable ex) {
          logger.error(ex.getMessage());
        }
      });
    });
  }
}

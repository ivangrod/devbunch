package com.devbunch.graphmodel.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import com.devbunch.model.FeedItem;

@Service
public class Receiver {

  private static final Logger LOG = LoggerFactory.getLogger(Receiver.class);

  @KafkaListener(topics = "${app.topic}")
  public void listen(@Payload FeedItem feedItem) {
    LOG.info("Received feed = [{}]", feedItem);
  }

}

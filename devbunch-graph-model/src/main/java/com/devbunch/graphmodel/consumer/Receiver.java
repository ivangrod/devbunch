package com.devbunch.graphmodel.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import com.devbunch.graphmodel.domain.Post;
import com.devbunch.graphmodel.service.PostService;
import com.devbunch.model.FeedItem;
import ma.glasnost.orika.MapperFacade;

@Service
public class Receiver {

  private static final Logger LOG = LoggerFactory.getLogger(Receiver.class);

  @Autowired
  private MapperFacade mapper;

  @Autowired
  private PostService postService;

  @KafkaListener(topics = "${app.topic}")
  public void listen(@Payload FeedItem feedItem) {

    LOG.info("Received feed = [{}]", feedItem);
    postService.savePost(mapper.map(feedItem, Post.class));
  }
}

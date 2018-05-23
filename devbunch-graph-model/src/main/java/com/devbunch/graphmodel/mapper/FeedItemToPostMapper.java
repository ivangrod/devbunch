package com.devbunch.graphmodel.mapper;

import org.springframework.stereotype.Component;
import com.devbunch.graphmodel.domain.Post;
import com.devbunch.graphmodel.domain.Topic;
import com.devbunch.model.FeedItem;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.ConfigurableMapper;

@Component
public class FeedItemToPostMapper extends ConfigurableMapper {

  @Override
  protected void configure(MapperFactory factory) {

    factory.classMap(FeedItem.class, Post.class).byDefault()
        .customize(new CustomMapper<FeedItem, Post>() {
          @Override
          public void mapAtoB(FeedItem feedItem, Post post, MappingContext context) {

            super.mapAtoB(feedItem, post, context);

            if (feedItem.getTopics() != null) {
              feedItem.getTopics().forEach(topicName -> {
                Topic topic = new Topic();
                topic.setName(topicName);
                post.taggedWith(topic, 1.0);
              });
            }
          }
        }).register();
  }
}

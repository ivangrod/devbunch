package com.devbunch.feedcollector.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "feed.collector")
public class FeedCollectorProperties {

  private final Writer writerProperties = new Writer();

  public Writer getWriterProperties() {
    return writerProperties;
  }

  public static class Writer {

    private String topicName;

    public String getTopicName() {
      return topicName;
    }

    public void setTopicName(String topic) {
      this.topicName = topic;
    }
  }
}

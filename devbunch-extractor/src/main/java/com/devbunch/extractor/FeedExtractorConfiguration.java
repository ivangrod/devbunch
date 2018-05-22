package com.devbunch.extractor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.kstream.ValueTransformerSupplier;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import com.devbunch.extractor.crawler.config.CrawlerConfig;
import com.devbunch.extractor.crawler.service.ExtractorFactoryMethod;
import com.devbunch.extractor.crawler.service.ExtractorService;
import com.devbunch.model.FeedItem;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class FeedExtractorConfiguration {

  @Autowired
  private KafkaProperties kafkaProperties;

  @Autowired
  private CrawlerConfig crawlerConfig;

  @SuppressWarnings("resource")
  @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
  public StreamsConfig kStreamsConfigs() {
    Map<String, Object> props = new HashMap<>();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "devbunch-extractor");
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
        new JsonSerde<>(FeedItem.class).getClass());
    props.put(JsonDeserializer.DEFAULT_KEY_TYPE, String.class);
    props.put(JsonDeserializer.DEFAULT_VALUE_TYPE, FeedItem.class);
    return new StreamsConfig(props);
  }

  @Bean
  public KStream<String, FeedItem> kStreamJson(StreamsBuilder builder) {

    KStream<String, FeedItem> feedItemStream = builder.stream("feed-item",
        Consumed.with(Serdes.String(), new JsonSerde<>(FeedItem.class)));

    KStream<String, FeedItem> feedItemExtractedStream =
        feedItemStream.transformValues(new FeedItemExtractor());

    feedItemExtractedStream.to("feed-item-complete",
        Produced.with(Serdes.String(), new JsonSerde<>(FeedItem.class)));

    return feedItemStream;
  }

  public class FeedItemExtractor implements ValueTransformerSupplier<FeedItem, FeedItem> {

    @Override
    public ValueTransformer<FeedItem, FeedItem> get() {
      return new ValueTransformer<FeedItem, FeedItem>() {

        @Override
        public void init(ProcessorContext context) {}

        @Override
        public FeedItem transform(FeedItem feedItemToExtract) {

          String originFeed = feedItemToExtract.getOrigin();
          ExtractorService customExtractor = ExtractorFactoryMethod.getExtractor(originFeed,
              feedItemToExtract.getUri(), Optional.ofNullable(crawlerConfig.crawler(originFeed)));

          if (customExtractor != null) {
            feedItemToExtract.setContent(customExtractor.extractContent());
            feedItemToExtract.setTopics(customExtractor.extractTopics());
          }

          return feedItemToExtract;
        }

        @Override
        public FeedItem punctuate(long timestamp) {
          return null;
        }

        @Override
        public void close() {}
      };
    }
  }
}

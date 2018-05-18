package com.devbunch.feedcollector.processor;

import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import com.devbunch.feedcollector.converter.FeedItemConverter;
import com.devbunch.model.FeedItem;
import com.rometools.rome.feed.synd.SyndEntry;

@Component
public class FeedItemProcessor implements ItemProcessor<Entry<String, SyndEntry>, FeedItem> {

  private static final Logger logger = LoggerFactory.getLogger(FeedItemProcessor.class);

  @Override
  public FeedItem process(final Entry<String, SyndEntry> feed) throws Exception {

    FeedItem feedItem = null;

    try {
      feedItem = FeedItemConverter.convertEntryToFeedItem(feed.getKey(), feed.getValue());
    } catch (Exception exc) {
      logger.error(
          "An error has been produced at " + feed.getValue().getLink() + " in processor method",
          exc);
    }

    return feedItem;
  }
}

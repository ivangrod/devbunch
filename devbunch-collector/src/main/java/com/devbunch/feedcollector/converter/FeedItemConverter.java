package com.devbunch.feedcollector.converter;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import com.devbunch.model.FeedItem;
import com.rometools.rome.feed.synd.SyndEntry;

public final class FeedItemConverter {

  private FeedItemConverter() {
    throw new IllegalStateException("Private access to constructor");
  }

  public static FeedItem convertEntryToFeedItem(final String origin, final SyndEntry entry) {

    FeedItem.FeedItemBuilder builder = FeedItem.builder();

    builder.title(entry.getTitle()).uri(entry.getLink()).creator(entry.getAuthor())
        .origin(origin.toUpperCase());

    builder.generatedId(md5FromString(entry.getLink()));

    if (Optional.ofNullable(entry.getPublishedDate()).isPresent()) {
      builder.publicationTimestamp(
          entry.getPublishedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }
    builder.collectTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime());

    // TODO TOPICS & CONTENT

    return builder.build();
  }

  private static String md5FromString(final String link) {
    return !StringUtils.isEmpty(link) ? DigestUtils.md5DigestAsHex(link.getBytes()) : null;
  }
}

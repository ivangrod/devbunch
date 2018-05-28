package com.devbunch.feedcollector.converter;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;
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
      builder.publicationTimestamp(entry.getPublishedDate());
    }
    builder.collectTimestamp(Date.from(Instant.now()));

    if (!CollectionUtils.isEmpty(entry.getContents())) {
      StringBuilder strBuilder = new StringBuilder();
      entry.getContents().forEach(content -> strBuilder.append(content.getValue()));
      builder.content(strBuilder.toString());
    }

    if (!CollectionUtils.isEmpty(entry.getCategories())) {
      Set<String> topicNameCollection = entry.getCategories().stream()
          .map(syndCategory -> syndCategory.getName()).collect(Collectors.toSet());
      builder.topics(topicNameCollection);
    }

    return builder.build();
  }

  private static String md5FromString(final String link) {
    return !StringUtils.isEmpty(link) ? DigestUtils.md5DigestAsHex(link.getBytes()) : null;
  }
}

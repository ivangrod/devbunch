package com.devbunch.feedcollector.reader;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.xml.sax.InputSource;
import com.google.common.collect.Maps;
import com.rometools.opml.feed.opml.Opml;
import com.rometools.opml.feed.opml.Outline;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.WireFeedInput;
import com.rometools.rome.io.XmlReader;

@Component
public class FeedItemReader extends ItemReaderAdapter<Entry<String, SyndEntry>> {

  private static final Logger logger = LoggerFactory.getLogger(FeedItemReader.class);

  @Value("classpath:opml/engineering_blogs.opml")
  private Resource resourceOpml;

  private List<Outline> outlinesToRead;

  private String currentOriginToRead;

  private List<SyndEntry> currentEntriesToRead;

  @PostConstruct
  public void init() {

    super.setTargetObject(this);
    super.setTargetMethod("read");

    WireFeedInput input = new WireFeedInput();
    List<Outline> outlines = null;
    try {
      Opml feed = (Opml) input.build(new InputSource(this.resourceOpml.getInputStream()));
      outlines = feed.getOutlines();
    } catch (IllegalArgumentException | IOException | FeedException exc) {
      logger.error("An error has been produced while opml was processed", exc);
    }

    this.outlinesToRead = !CollectionUtils.isEmpty(outlines) ? outlines.get(0).getChildren()
        : Collections.emptyList();
  }

  @Override
  public Entry<String, SyndEntry> read() throws Exception {

    if (!CollectionUtils.isEmpty(this.currentEntriesToRead)) {
      return this.analyzeEntriesUpdatedToDate();

    } else if (!CollectionUtils.isEmpty(this.outlinesToRead)) {
      initLoadOfCurrentEntriesToRead();
      return this.read();
    }
    return null;
  }

  private void initLoadOfCurrentEntriesToRead() {

    Outline outlineForFeed = this.outlinesToRead.remove(0);
    Entry<String, SyndFeed> feedWithOrigin =
        this.buildOriginAndFeedPair(outlineForFeed.getText(), outlineForFeed.getXmlUrl());

    this.currentOriginToRead = feedWithOrigin.getKey();

    Optional.ofNullable(feedWithOrigin.getValue())
        .ifPresent(feed -> this.currentEntriesToRead = feed
            .getEntries().stream().sorted(Comparator
                .comparing(SyndEntry::getPublishedDate, nullsLast(naturalOrder())).reversed())
            .collect(Collectors.toList()));
  }

  private Entry<String, SyndFeed> buildOriginAndFeedPair(final String origin,
      final String feedUrl) {

    SyndFeed feed = null;
    try {
      SyndFeedInput input = new SyndFeedInput();
      feed = input.build(new XmlReader(new URL(feedUrl)));
    } catch (IllegalArgumentException | FeedException | IOException exc) {
      logger.error("Url of feed [{}]", feedUrl);
      logger.error("An error has been produced while opml was processed.", exc);
    }

    return Maps.immutableEntry(origin.toUpperCase(), feed);
  }

  private Entry<String, SyndEntry> analyzeEntriesUpdatedToDate() {

    SyndEntry entryToRead = this.currentEntriesToRead.remove(0);

    try {

      // TODO: Integrate solution of feed-integration spring to store metadata of
      // feeds

      // if (entryToRead.getPublishedDate() != null) {
      // if
      // (bundleItemService.isEntriesByOriginUpdatedToDate(this.currentOriginToRead,
      // entryToRead.getLink(),
      // entryToRead.getPublishedDate())) {
      // this.currentEntriesToRead.clear();
      // return this.read();
      // }
      // }

    } catch (Exception e) {
      // TODO: handle exception
    }

    return new AbstractMap.SimpleImmutableEntry<>(this.currentOriginToRead, entryToRead);
  }
}

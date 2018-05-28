package com.devbunch.extractor.crawler.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import com.devbunch.extractor.crawler.ICrawler;
import com.devbunch.extractor.crawler.enums.FieldNameExtractor;

public class ExternalExtractorService extends ExtractorService {

  private ICrawler currentCrawler;

  private Map<FieldNameExtractor, Object> currentContentCrawl = new HashMap<>();

  public ExternalExtractorService(String origin, String link, ICrawler crawler) {
    super(origin, link);
    this.currentCrawler = crawler;
  }

  @Override
  public String extractContent() {

    Optional<Object> opContent = this.getValueByFieldNameFromCrawler(FieldNameExtractor.CONTENT);
    return (opContent.isPresent()) ? (String) opContent.get() : null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Set<String> extractTopics() {
    Optional<Object> opCategories = this.getValueByFieldNameFromCrawler(FieldNameExtractor.TAGS);
    return (opCategories.isPresent()) ? (Set<String>) opCategories.get() : new HashSet<>();

  }

  private Optional<Object> getValueByFieldNameFromCrawler(final FieldNameExtractor fieldName) {

    if (currentContentCrawl.isEmpty()) {
      this.currentContentCrawl =
          this.currentCrawler != null ? this.currentCrawler.crawl(getLink()) : new HashMap<>();
    }

    return Optional.ofNullable(
        (this.currentContentCrawl != null && this.currentContentCrawl.containsKey(fieldName))
            ? this.currentContentCrawl.get(fieldName)
            : null);
  }
}

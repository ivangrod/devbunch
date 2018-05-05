package com.devbunch.extractor.crawler.service;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class ExtractorService {

  private String origin;

  private String link;

  public ExtractorService(final String link) {
    this.link = link;
  }

  public abstract String extractContent();

  public abstract Set<String> extractTopics();
}

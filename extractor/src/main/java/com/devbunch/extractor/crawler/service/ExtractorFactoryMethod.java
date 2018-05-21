package com.devbunch.extractor.crawler.service;

import java.util.Optional;

import com.devbunch.extractor.crawler.ICrawler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExtractorFactoryMethod {

  public static ExtractorService getExtractor(final String origin, final String link,
      Optional<ICrawler> crawler) {

    if (crawler.isPresent()) {
      log.debug("Feed external with origin [{}]", origin);
      return new ExternalExtractorService(origin, link, crawler.get());
    }

    return null;
  }
}

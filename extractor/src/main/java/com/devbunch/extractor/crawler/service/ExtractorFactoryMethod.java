package com.devbunch.extractor.crawler.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.devbunch.extractor.crawler.ICrawler;

public class ExtractorFactoryMethod {

  private static final Logger logger = LoggerFactory.getLogger(ExtractorFactoryMethod.class);

  public static ExtractorService getExtractor(final String origin, final String link,
      Optional<ICrawler> crawler) {

    if (crawler.isPresent()) {
      logger.info("Feed external with origin [{}]", origin);
      return new ExternalExtractorService(origin, link, crawler.get());
    }

    return null;
  }
}

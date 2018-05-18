package com.devbunch.extractor.crawler;

import java.util.Map;
import com.devbunch.extractor.crawler.enums.FieldNameExtractor;

@FunctionalInterface
public interface ICrawler {

  public Map<FieldNameExtractor, Object> crawl(String link);
}

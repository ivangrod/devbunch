package com.devbunch.extractor.crawler.config;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import com.devbunch.extractor.crawler.CommonCrawler;
import com.devbunch.extractor.crawler.ICrawler;
import com.devbunch.extractor.crawler.persistence.model.ExternalExtractorDataEntity;
import com.devbunch.extractor.crawler.persistence.repository.ExternalExtractorDataJpaRepository;

@Configuration
public class CrawlerConfig {

  @Autowired
  private ExternalExtractorDataJpaRepository externalExtractorDataRepository;

  public ICrawler crawler(final String origin) {

    ICrawler crawlerResult = null;
    Optional<ExternalExtractorDataEntity> externalExtractorDataOptional =
        externalExtractorDataRepository.findByOrigin(origin);

    if (externalExtractorDataOptional.isPresent()) {
      ExternalExtractorDataEntity externalExtractorData = externalExtractorDataOptional.get();
      crawlerResult = new CommonCrawler(externalExtractorData.getSelectorContent(),
          externalExtractorData.getSelectorCategories());
    }

    return crawlerResult;
  }
}

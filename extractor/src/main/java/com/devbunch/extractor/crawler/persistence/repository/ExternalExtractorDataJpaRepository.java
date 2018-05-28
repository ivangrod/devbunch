package com.devbunch.extractor.crawler.persistence.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.devbunch.extractor.crawler.persistence.model.ExternalExtractorDataEntity;

public interface ExternalExtractorDataJpaRepository
    extends JpaRepository<ExternalExtractorDataEntity, Long> {

  Optional<ExternalExtractorDataEntity> findByOrigin(String origin);
}

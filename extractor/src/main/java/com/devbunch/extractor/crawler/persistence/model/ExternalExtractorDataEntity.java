package com.devbunch.extractor.crawler.persistence.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = ExternalExtractorDataEntity.TABLE_NAME)
public class ExternalExtractorDataEntity implements Serializable {

  private static final long serialVersionUID = 513104478912645703L;

  public static final String TABLE_NAME = "external_extractor_data";

  public static final String ORIGIN_COLUMN_NAME = "origin";
  public static final String SELECTOR_CONTENT_COLUMN_NAME = "selector_content";
  public static final String SELECTOR_CATEGORIES_COLUMN_NAME = "selector_categories";

  @Id
  @Column(name = "id", nullable = false, unique = true)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = ORIGIN_COLUMN_NAME, unique = true)
  private String origin;

  @Column(name = SELECTOR_CONTENT_COLUMN_NAME)
  private String selectorContent;

  @Column(name = SELECTOR_CATEGORIES_COLUMN_NAME)
  private String selectorCategories;
}

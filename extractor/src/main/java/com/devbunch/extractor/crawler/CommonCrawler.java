package com.devbunch.extractor.crawler;

import com.devbunch.extractor.crawler.dto.PostDTO;

public class CommonCrawler extends PostCrawler {

  private String selectorContent;
  private String selectorCategories;

  public CommonCrawler(String selectorContent, String selectorCategories) {
    this.selectorContent = selectorContent;
    this.selectorCategories = selectorCategories;
  }

  @Override
  public PostDTO retrievePostDTOByType(String urlPageFrom) {
    return retrievePostDTO(urlPageFrom, this.selectorContent, this.selectorCategories);
  }
}

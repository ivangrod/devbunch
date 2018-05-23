package com.devbunch.extractor.crawler.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO implements Serializable {

  private static final long serialVersionUID = -337933952152225256L;

  private String content;

  private String link;

  /*
   * Output field from disambiguate process to specify technical tags
   */
  private Set<String> tags;

  /*
   * Output field from disambiguate process to specify description labels
   */
  private Set<String> labels;

  /*
   * Input field in disambiguate process
   */
  private List<String> markers;
}

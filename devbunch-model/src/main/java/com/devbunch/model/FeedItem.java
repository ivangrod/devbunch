package com.devbunch.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedItem implements Serializable {

  private static final long serialVersionUID = -5898378247168511380L;

  private String generatedId;

  private String title;

  private String uri;

  private String creator;

  private String origin;

  private String content;

  private Date collectTimestamp;

  private Date publicationTimestamp;

  private Set<String> topics;
}

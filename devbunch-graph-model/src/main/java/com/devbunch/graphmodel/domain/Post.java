package com.devbunch.graphmodel.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class Post {

  @Id
  @GeneratedValue
  private Long id;

  private String generatedId;

  private String uri;

  private String origin;

  private LocalDateTime collectTimestamp;

  private LocalDateTime publicationTimestamp;

  @Relationship(type = "TAG_WITH")
  private List<Topic> topics = new ArrayList<>();
}

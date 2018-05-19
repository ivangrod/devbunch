package com.devbunch.graphmodel.domain;

import java.util.List;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class Topic {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @JsonIgnoreProperties("topic")
  @Relationship(type = "TAG_WITH", direction = Relationship.INCOMING)
  private List<Tag> tags;
}

package com.devbunch.graphmodel.domain;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RelationshipEntity(type = "TAG_WITH")
public class Tag {

  @Id
  @GeneratedValue
  private Long id;

  private Double weight;

  @StartNode
  private Post post;

  @EndNode
  private Topic topic;

}

package com.devbunch.graphmodel.domain;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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

  public Tag(Post post, Topic topic) {
    this.post = post;
    this.topic = topic;
  }

  public Tag(Post post, Topic topic, Double weight) {
    this.post = post;
    this.topic = topic;
    this.weight = weight;
  }

  @Override
  public String toString() {
    return String.format("[%s] posted with tag [%s] - [%s]", post, weight, topic);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Tag tag = (Tag) o;

    if (topic != null ? !topic.equals(tag.topic) : tag.topic != null)
      return false;
    if (post != null ? !post.equals(tag.post) : tag.post != null)
      return false;
    return !(weight != null ? !weight.equals(tag.weight) : tag.weight != null);
  }

  @Override
  public int hashCode() {
    int result = topic != null ? topic.hashCode() : 0;
    result = 31 * result + (post != null ? post.hashCode() : 0);
    result = 31 * result + (weight != null ? weight.hashCode() : 0);
    return result;
  }
}

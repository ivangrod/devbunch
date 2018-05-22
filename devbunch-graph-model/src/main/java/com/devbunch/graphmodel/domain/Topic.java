package com.devbunch.graphmodel.domain;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class Topic {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  // @JsonIgnoreProperties("topic")
  @Relationship(type = "TAG_WITH", direction = Relationship.INCOMING)
  private List<Tag> tags = new ArrayList<Tag>();

  public void addTag(Tag tag) {
    tags.add(tag);
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (!(o instanceof Topic)) {
      return false;
    }
    Topic topic = (Topic) o;
    if (id != null ? !id.equals(topic.id) : topic.id != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}

package com.devbunch.graphmodel.domain;

import java.util.ArrayList;
import java.util.Date;
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
public class Post {

  @Id
  @GeneratedValue
  private Long id;

  private String generatedId;

  private String uri;

  private String origin;

  private Date collectTimestamp;

  private Date publicationTimestamp;

  @Relationship(type = "TAG_WITH", direction = Relationship.OUTGOING)
  private List<Tag> tags = new ArrayList<Tag>();

  public Tag taggedWith(Topic topic, Double weigth) {
    final Tag tag = new Tag(this, topic, weigth);
    tags.add(tag);
    topic.addTag(tag);
    return tag;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (!(o instanceof Post)) {
      return false;
    }
    Post post = (Post) o;
    if (id != null ? !id.equals(post.id) : post.id != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}

package com.devbunch.graphmodel.repository;

import java.util.Collection;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import com.devbunch.graphmodel.domain.Topic;

public interface TopicRepository extends Neo4jRepository<Topic, Long> {

  Collection<Topic> findByNameLikeIgnoreCase(@Param("name") String name);

  @Query("MATCH (t:Topic)<-[r:TAG_WITH]-(p:Post) RETURN t,r,p LIMIT {limit}")
  Collection<Topic> graph(@Param("limit") int limit);
}

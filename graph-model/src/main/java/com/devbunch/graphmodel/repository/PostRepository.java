package com.devbunch.graphmodel.repository;

import java.util.Collection;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import com.devbunch.graphmodel.domain.Post;

public interface PostRepository extends Neo4jRepository<Post, Long> {

  Optional<Post> findByGenerateId(String generateId);

  Collection<Post> findByOriginIgnoreCase(String origin);
}

package com.devbunch.graphmodel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.devbunch.graphmodel.domain.Post;
import com.devbunch.graphmodel.repository.PostRepository;

@Service
public class PostService implements IPostService {

  private PostRepository postRepository;

  @Autowired
  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Transactional
  @Override
  public void savePost(Post post) {
    postRepository.save(post);
  }
}

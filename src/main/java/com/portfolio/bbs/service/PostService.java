package com.portfolio.bbs.service;

import com.portfolio.bbs.entity.Post;
import com.portfolio.bbs.form.PostForm;
import com.portfolio.bbs.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public List<Post> findAll() {
        return postRepository.findAllByOrderByCreatedAtDescIdDesc();
    }

    @Transactional
    public void create(PostForm postForm) {
        Post post = new Post(
                postForm.getName().trim(),
                postForm.getContent().trim()
        );

        postRepository.save(post);
    }
}
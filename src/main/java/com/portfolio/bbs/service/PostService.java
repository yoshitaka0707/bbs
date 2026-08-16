package com.portfolio.bbs.service;

import com.portfolio.bbs.entity.Post;
import com.portfolio.bbs.form.PostForm;
import com.portfolio.bbs.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    @Transactional
    public int addLike(Long postId) {
        Post post = findPost(postId);
        post.incrementLikeCount();

        return post.getLikeCount();
    }

    @Transactional
    public int removeLike(Long postId) {
        Post post = findPost(postId);
        post.decrementLikeCount();

        return post.getLikeCount();
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "投稿が見つかりません"
                        )
                );
    }
}
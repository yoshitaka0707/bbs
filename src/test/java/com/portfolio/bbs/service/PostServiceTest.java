package com.portfolio.bbs.service;

import com.portfolio.bbs.entity.Post;
import com.portfolio.bbs.form.PostForm;
import com.portfolio.bbs.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void findAllReturnsRepositoryResult() {
        List<Post> posts = List.of(
                new Post("user1", "content1"),
                new Post("user2", "content2")
        );

        when(postRepository.findAllByOrderByCreatedAtDescIdDesc())
                .thenReturn(posts);

        List<Post> result = postService.findAll();

        assertSame(posts, result);
        verify(postRepository)
                .findAllByOrderByCreatedAtDescIdDesc();
    }

    @Test
    void createSavesPostWithTrimmedValues() {
        PostForm postForm = new PostForm();
        postForm.setName("  test user  ");
        postForm.setContent("  test content  ");

        postService.create(postForm);

        ArgumentCaptor<Post> postCaptor =
                ArgumentCaptor.forClass(Post.class);

        verify(postRepository).save(postCaptor.capture());

        Post savedPost = postCaptor.getValue();

        assertEquals("test user", savedPost.getName());
        assertEquals("test content", savedPost.getContent());
    }

    @Test
    void addLikeIncrementsLikeCount() {
        Post post = new Post("test user", "test content");

        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        int result = postService.addLike(1L);

        assertEquals(1, result);
        assertEquals(1, post.getLikeCount());
    }

    @Test
    void removeLikeDecrementsLikeCount() {
        Post post = new Post("test user", "test content");
        post.incrementLikeCount();

        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        int result = postService.removeLike(1L);

        assertEquals(0, result);
        assertEquals(0, post.getLikeCount());
    }

    @Test
    void removeLikeDoesNotMakeCountNegative() {
        Post post = new Post("test user", "test content");

        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        int result = postService.removeLike(1L);

        assertEquals(0, result);
        assertEquals(0, post.getLikeCount());
    }

    @Test
    void addLikeThrowsNotFoundWhenPostDoesNotExist() {
        when(postRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> postService.addLike(999L)
                );

        assertEquals(
                HttpStatus.NOT_FOUND,
                exception.getStatusCode()
        );
    }
}
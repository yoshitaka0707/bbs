package com.portfolio.bbs.controller;

import com.portfolio.bbs.config.SecurityConfig;
import com.portfolio.bbs.form.PostForm;
import com.portfolio.bbs.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PostController.class)
@Import(SecurityConfig.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Test
    void indexDisplaysPostList() throws Exception {
        when(postService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("postForm"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("likedPostIds"));
    }

    @Test
    void createRedirectsWhenInputIsValid() throws Exception {
        mockMvc.perform(
                        post("/posts")
                                .with(csrf())
                                .param("name", "test user")
                                .param("content", "test content")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(postService).create(any(PostForm.class));
    }

    @Test
    void createDisplaysErrorsWhenInputIsBlank()
            throws Exception {

        when(postService.findAll()).thenReturn(List.of());

        mockMvc.perform(
                        post("/posts")
                                .with(csrf())
                                .param("name", "")
                                .param("content", "")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(
                        model().attributeHasFieldErrors(
                                "postForm",
                                "name",
                                "content"
                        )
                );

        verify(postService, never())
                .create(any(PostForm.class));
    }

    @Test
    void createRejectsRequestWithoutCsrfToken()
            throws Exception {

        mockMvc.perform(
                        post("/posts")
                                .param("name", "test user")
                                .param("content", "test content")
                )
                .andExpect(status().isForbidden());

        verify(postService, never())
                .create(any(PostForm.class));
    }

    @Test
    void toggleLikeAddsLikeOnFirstRequest()
            throws Exception {

        MockHttpSession session = new MockHttpSession();

        when(postService.addLike(1L)).thenReturn(1);

        mockMvc.perform(
                        post("/posts/1/likes")
                                .session(session)
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likeCount").value(1))
                .andExpect(jsonPath("$.liked").value(true));

        verify(postService).addLike(1L);
    }

    @Test
    void toggleLikeRemovesLikeOnSecondRequest()
            throws Exception {

        MockHttpSession session = new MockHttpSession();

        when(postService.addLike(1L)).thenReturn(1);
        when(postService.removeLike(1L)).thenReturn(0);

        mockMvc.perform(
                        post("/posts/1/likes")
                                .session(session)
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.liked").value(true));

        mockMvc.perform(
                        post("/posts/1/likes")
                                .session(session)
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likeCount").value(0))
                .andExpect(jsonPath("$.liked").value(false));

        verify(postService).addLike(1L);
        verify(postService).removeLike(1L);
    }

    @Test
    void toggleLikeRejectsRequestWithoutCsrfToken()
            throws Exception {

        mockMvc.perform(post("/posts/1/likes"))
                .andExpect(status().isForbidden());

        verify(postService, never()).addLike(1L);
        verify(postService, never()).removeLike(1L);
    }
}
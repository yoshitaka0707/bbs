package com.portfolio.bbs.controller;

import com.portfolio.bbs.form.PostForm;
import com.portfolio.bbs.service.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class PostController {

    private static final String LIKED_POST_IDS =
            "likedPostIds";

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        model.addAttribute("postForm", new PostForm());
        model.addAttribute("posts", postService.findAll());
        model.addAttribute(
                "likedPostIds",
                getLikedPostIds(session)
        );
        return "index";
    }

    @PostMapping("/posts")
    public String create(
            @Valid @ModelAttribute PostForm postForm,
            BindingResult bindingResult,
            Model model,
            HttpSession session
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("posts", postService.findAll());
            model.addAttribute(
                    "likedPostIds",
                    getLikedPostIds(session)
            );
            return "index";
        }

        postService.create(postForm);

        return "redirect:/";
    }

    @PostMapping("/posts/{id}/likes")
    @ResponseBody
    public Map<String, Object> toggleLike(
            @PathVariable("id") Long id,
            HttpSession session
    ) {
        Set<Long> likedPostIds = getLikedPostIds(session);

        synchronized (likedPostIds) {
            boolean liked;
            int likeCount;

            if (likedPostIds.contains(id)) {
                likeCount = postService.removeLike(id);
                likedPostIds.remove(id);
                liked = false;
            } else {
                likeCount = postService.addLike(id);
                likedPostIds.add(id);
                liked = true;
            }

            session.setAttribute(
                    LIKED_POST_IDS,
                    likedPostIds
            );

            return Map.of(
                    "likeCount", likeCount,
                    "liked", liked
            );
        }
    }

    @SuppressWarnings("unchecked")
    private Set<Long> getLikedPostIds(HttpSession session) {
        Set<Long> likedPostIds =
                (Set<Long>) session.getAttribute(LIKED_POST_IDS);

        if (likedPostIds == null) {
            likedPostIds = new HashSet<>();
            session.setAttribute(
                    LIKED_POST_IDS,
                    likedPostIds
            );
        }

        return likedPostIds;
    }
}
package com.portfolio.bbs.controller;

import com.portfolio.bbs.form.PostForm;
import com.portfolio.bbs.service.PostService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("postForm", new PostForm());
        model.addAttribute("posts", postService.findAll());

        return "index";
    }

    @PostMapping("/posts")
    public String create(
            @Valid @ModelAttribute PostForm postForm,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("posts", postService.findAll());
            return "index";
        }

        postService.create(postForm);

        return "redirect:/";
    }
}
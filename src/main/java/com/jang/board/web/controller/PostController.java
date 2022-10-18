package com.jang.board.web.controller;

import com.jang.board.web.controller.form.PostForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class PostController {
    @GetMapping("/post")
    public String postForm(@ModelAttribute("postForm") PostForm postForm) {
        return "post";
    }
}

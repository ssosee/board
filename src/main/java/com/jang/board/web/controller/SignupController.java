package com.jang.board.web.controller;

import com.jang.board.web.controller.form.SignupForm;
import com.jang.board.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class SignupController {
    private final LoginService loginService;

    @GetMapping("/signup")
    public String signupForm(@ModelAttribute("signupForm") SignupForm signupForm) {
        return "signupForm";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute SignupForm signupForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "signupForm";
        }

        log.info(signupForm.toString());
        loginService.signup(signupForm.getUserId(), signupForm.getPassword(), signupForm.getEmail(), signupForm.getPhone());

        return "redirect:/member/login";
    }
}

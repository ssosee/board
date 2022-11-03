package com.jang.board.web.controller.error;

import com.jang.board.exception.UserException;
import com.jang.board.web.controller.form.SignupForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(UserException.class)
    public ModelAndView signup(HttpServletRequest request, UserException e) {

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("signupError", e.getMessage());
        modelAndView.addObject("signupErrorURL", request.getRequestURL());
        modelAndView.setViewName("redirect:/member/signup");

        return modelAndView;
    }
}

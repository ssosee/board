package com.jang.board.web.controller.error;

import com.jang.board.exception.LoginException;
import com.jang.board.exception.SignupException;
import com.jang.board.web.controller.form.LoginForm;
import com.jang.board.web.controller.form.SignupForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(SignupException.class)
    public ModelAndView signupExHandler(SignupException e) {

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("signupEx", e.getMessage());
        modelAndView.addObject("signupForm", new SignupForm()); //뷰에 더미 데이터 매칭

        modelAndView.setViewName("signupForm");

        return modelAndView;
    }

    @ExceptionHandler(LoginException.class)
    public ModelAndView loginExHandler(LoginException e) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("loginEx", e.getMessage());
        modelAndView.addObject("loginForm", new LoginForm()); //뷰에 더미 데이터 매칭
        modelAndView.setViewName("loginForm");

        return modelAndView;
    }
}

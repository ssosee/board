package com.jang.board.web.controller;

import com.jang.board.web.controller.form.LoginForm;
import com.jang.board.domain.Member;
import com.jang.board.service.LoginService;
import com.jang.board.web.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm loginForm, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/home") String redirectUrl,
                        HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        Optional<Member> loginMember = loginService.login(loginForm.getUserId(), loginForm.getPassword());
        if(loginMember.isEmpty()) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");

            return "login";
        }

        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession(true);
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMEBER, loginMember.get());

        return "redirect:"+redirectUrl;
    }
}

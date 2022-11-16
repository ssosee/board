package com.jang.board.web.controller;

import com.jang.board.web.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/member")
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();
        if(session == null) {
            return "redirect:/home?redirectURL="+requestURI;
        }

        session.removeAttribute(SessionConst.LOGIN_MEMBER);

        return "redirect:/home";
    }
}

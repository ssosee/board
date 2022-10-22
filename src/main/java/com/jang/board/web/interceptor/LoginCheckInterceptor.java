package com.jang.board.web.interceptor;

import com.jang.board.web.session.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestUrl = request.getRequestURI();

        log.info("인증 체크 인터셉터 실행={}", requestUrl);
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMEBER) == null) {
            log.info("미인증 사용자 요청");

            //로그인으로 redirect
            response.sendRedirect("/member/login?redirectURL="+requestUrl);
            return false;
        } else if (requestUrl.equals("/")) {
            response.sendRedirect("/home");
        }

        return true;
    }
}

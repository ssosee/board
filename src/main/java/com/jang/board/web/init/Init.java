package com.jang.board.web.init;

import com.jang.board.domain.Member;
import com.jang.board.repository.PostRepository;
import com.jang.board.service.LoginService;
import com.jang.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Init {
    private final LoginService loginService;
    private final PostService postService;
    private final PostRepository postRepository;

    @PostConstruct
    public void Init() {
        Long memberId = loginService.singnup("admin", "1", "howisitgoing@kakao.com", "01012341234");

        List<String> fileNames = Arrays.asList("사진1","사진2","사진3");
        for(int i = 0; i < 134; i++) {
            Long postId = postService.addPost("안녕하세요"+i, "테스트입니다."+i, fileNames, memberId);
        }
    }
}

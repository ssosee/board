package com.jang.board.service;

import com.jang.board.domain.Member;
import com.jang.board.domain.Post;
import com.jang.board.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * <a href="https://cantcoding.tistory.com/m/78">영속성 참고</a>
 */
@SpringBootTest
@Transactional
class PostServiceImplTest {

    @Autowired
    PostService postService;
    @Autowired
    LoginService loginService;
    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("게시글 작성 테스트")
    void 게시글_작성_테스트() {

        List<String> fileNames = Arrays.asList("사진1","사진2","사진3");
        Long memberId = loginService.singnup("dlwlrma", "dlwlrma!23", "dlwlrma@kakao.com", "010-1234-1234");
        Optional<Member> loginMember = loginService.login("dlwlrma", "dlwlrma!23");

        Long postId = postService.addPost("안녕하세요", "테스트입니다.", fileNames, memberId);
        Optional<Post> optionalPost = postRepository.findById(postId);

        assertThat(optionalPost.get().getId()).isEqualTo(postId);
        assertThat(optionalPost.get().getMember()).isEqualTo(loginMember.get());
    }
}
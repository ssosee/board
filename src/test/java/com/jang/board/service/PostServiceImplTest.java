package com.jang.board.service;

import com.jang.board.domain.Member;
import com.jang.board.domain.Post;
import com.jang.board.repository.PostRepository;
import com.jang.board.web.file.FileStore;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * <a href="https://cantcoding.tistory.com/m/78">영속성 참고</a>
 */
@SpringBootTest
class PostServiceImplTest {

    @Autowired
    PostService postService;
    @Autowired
    LoginService loginService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    FileStore fileStore;

    @Test
    @DisplayName("게시글 작성 테스트")
    @Transactional
    void 게시글_작성_테스트() {
        List<String> fileNames = Arrays.asList("사진1","사진2","사진3");
        List<String> storeFileName = fileStore.createStoreFileName(new ArrayList<>(fileNames));
        Long memberId = loginService.signup("dlwlrma", "dlwlrma!23", "dlwlrma@kakao.com", "010-1234-1234");
        Optional<Member> loginMember = loginService.login("dlwlrma", "dlwlrma!23");

        Long postId = postService.addPost("안녕하세요", "테스트입니다.", fileNames, storeFileName, memberId);
        Optional<Post> optionalPost = postRepository.findById(postId);

        assertThat(optionalPost.get().getId()).isEqualTo(postId);
        assertThat(optionalPost.get().getMember()).isEqualTo(loginMember.get());
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    @Transactional
    void 게시글_수정_테스트() throws Exception {
        //given
        Long memberId = loginService.signup("dlwlrma", "dlwlrma!23", "dlwlrma@kakao.com", "010-1234-1234");
        List<String> fileNames = Arrays.asList("사진1","사진2","사진3");
        List<String> storeFileName = fileStore.createStoreFileName(new ArrayList<>(fileNames));
        Optional<Member> loginMember = loginService.login("dlwlrma", "dlwlrma!23");
        Long postId = postService.addPost("안녕하세요", "테스트입니다.", fileNames, storeFileName, memberId);
        //when
        List<String> updateFileNames = Arrays.asList("사진10","사진20","사진30");
        List<String> updateStoreFileName = fileStore.createStoreFileName(new ArrayList<>(updateFileNames));
        System.out.println("//=====수정 쿼리=====//");
        postService.updatePost(postId, "수정입니다.", "수정했습니다.", updateFileNames, updateStoreFileName);
        //then

    }
}
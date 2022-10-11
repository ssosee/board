package com.jang.board.service;

import com.jang.board.domain.Member;
import com.jang.board.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class LoginServiceImplTest {
    @Autowired
    LoginServiceImpl loginService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 테스트")
    public void 회원가입_테스트() {
        Long id = loginService.singnup("dlwlrma", "dlwlrma!23", "dlwlrma@kakao.com", "010-1234-1234");
        Optional<Member> findMember = memberRepository.findMemberByUserId("dlwlrma");

        Assertions.assertThat(findMember.get().getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("이미 존재하는 회원 테스트")
    public void 존재하는_회원_테스트() throws Exception {
        loginService.singnup("dlwlrma", "dlwlrma!23", "dlwlrma@kakao.com", "010-1234-1234");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            loginService.singnup("dlwlrma", "dlwlrma!23", "dlwlrma@kakao.com", "010-1234-1234");
        });
        assertEquals("이미 존재하는 회원 입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("로그인 테스트")
    public void 로그인_테스트() {
        Long id = loginService.singnup("dlwlrma", "dlwlrma!23", "dlwlrma@kakao.com", "010-1234-1234");
        Optional<Member> loginMember = loginService.login("dlwlrma", "dlwlrma!23");

        Assertions.assertThat(id).isEqualTo(loginMember.get().getId());
    }

    @Test
    @DisplayName("없는 회원 로그인")
    public void 없는_회원_로그인_테스트() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            loginService.login("dlwlrma", "dlwlrma!23");
        });
        assertEquals("아이디 또는 비밀번호를 확인해주세요.", exception.getMessage());
    }

    @Test
    @DisplayName("틀린 비밀번호 로그인")
    public void 틀린_비밀번호_로그인_테스트() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            loginService.login("dlwlrma", "dlwlrma!");
        });
        assertEquals("아이디 또는 비밀번호를 확인해주세요.", exception.getMessage());
    }
}
package com.jang.board.service;

import com.jang.board.domain.Member;
import com.jang.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long singnup(String userId, String password, String email, String phone) {

        validationSignup(userId);

        Member member = Member.builder()
                .userId(userId)
                .password(password)
                .email(email)
                .phone(phone)
                .build();

        memberRepository.save(member);

        return member.getId();
    }

    @Override
    public Optional<Member> login(String userId, String password) {
        return validationLogin(userId, password);
    }

    private void validationSignup(String userId) {
        Optional<Member> findMember = memberRepository.findMemberByUserId(userId);
        if(!findMember.isEmpty()) throw new IllegalArgumentException("이미 존재하는 회원 입니다.");
    }

    private Optional<Member> validationLogin(String userId, String password) {
        Optional<Member> findMember = memberRepository.findMemberByUserIdAndPassword(userId, password);
        if(findMember.isEmpty()) throw new IllegalArgumentException("아이디 또는 비밀번호를 확인해주세요.");

        return findMember;
    }
}

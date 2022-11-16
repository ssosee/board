package com.jang.board.repository;

import com.jang.board.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByUserIdAndPassword(String userId, String password);
    Optional<Member> findMemberByUserId(String userId);
}

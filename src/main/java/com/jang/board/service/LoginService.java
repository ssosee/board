package com.jang.board.service;

import com.jang.board.domain.Member;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface LoginService {
    Long signup(String userId, String password, String email, String phone);

    Optional<Member> login(String userId, String password);
}

package com.jang.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Test
    void 동적쿼리_테스트() {

        PageRequest pageRequest = PageRequest.of(0, 2);

        postRepository.findPostsByTitleOrContentOrAuthor(pageRequest, "안녕하세요.", null, null);
    }

}
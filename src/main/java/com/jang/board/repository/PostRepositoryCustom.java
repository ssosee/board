package com.jang.board.repository;

import com.jang.board.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> findPostsByTitleOrContentOrAuthor(Pageable pageable, String title, String content, String author);
}

package com.jang.board.service;

import com.jang.board.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Long addPost(String title, String content, List<String> uploadFileNames, List<String> storeFileNames, Long id);
    Page<Post> findPosts(Pageable pageable);
    Optional<Post> findPost(Long id);
}

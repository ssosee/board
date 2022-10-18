package com.jang.board.service;

import java.util.List;

public interface PostService {
    Long addPost(String title, String content, List<String> uploadFileName, Long id);
}

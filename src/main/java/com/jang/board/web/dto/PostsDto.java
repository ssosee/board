package com.jang.board.web.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostsDto {
    private Long id;
    private LocalDateTime writeTime;
    private String title;
    private String author;
}

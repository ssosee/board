package com.jang.board.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostsDto {
    private Long id;
    private String writeTime;
    private String title;
    private String author;
}

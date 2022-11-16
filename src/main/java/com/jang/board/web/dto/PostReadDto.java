package com.jang.board.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostReadDto {
    private String writeTime;
    private String title;
    private String content;
    private String author;
    private List<String> uploadFileName;
}

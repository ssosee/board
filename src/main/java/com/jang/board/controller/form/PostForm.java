package com.jang.board.controller.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PostForm {
    @NotNull(message = "제목을 입력해주세요.")
    private String title;
    @NotNull(message = "내용을 입력해주세요.")
    private String content;
    private String uploadFileName;
}

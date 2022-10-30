package com.jang.board.web.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostListForm {
    private String searchCode;
    private String searchParam;
}

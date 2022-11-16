package com.jang.board.web.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchPostForm {
    private String searchType;
    private String keyword;
}

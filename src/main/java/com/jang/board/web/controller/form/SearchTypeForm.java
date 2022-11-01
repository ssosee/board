package com.jang.board.web.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class SearchTypeForm {
    private String searchType;
    private String displayName;
    public static List<SearchTypeForm> createSearchTypeForm() {
//        final String TITLE ="제목";
//        final String CONTENT = "내용";
//        final String AUTHOR = "작성자";
//
//        List<SearchTypeForm> searchPostForms = new ArrayList<>();
//        searchPostForms.add(new SearchTypeForm(SearchType.title.toString(), TITLE));
//        searchPostForms.add(new SearchTypeForm(SearchType.content.toString(), CONTENT));
//        searchPostForms.add(new SearchTypeForm(SearchType.author.toString(), AUTHOR));


        List<SearchTypeForm> searchPostForms = new ArrayList<>();
        searchPostForms.add(new SearchTypeForm("title", "제목"));
        searchPostForms.add(new SearchTypeForm("content", "내용"));
        searchPostForms.add(new SearchTypeForm("author", "작성자"));


        return searchPostForms;
    }
}

package com.jang.board.web.controller.form;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PostForm {
    @NotNull(message = "제목을 입력해주세요.")
    private String title;
    @NotNull(message = "내용을 입력해주세요.")
    private String content;
    private List<MultipartFile> imageFile;
    private List<String> uploadFilename;

    public PostForm(String title, String content, List<String> uploadFilename) {
        this.title = title;
        this.content = content;
        this.uploadFilename = uploadFilename;
    }
}

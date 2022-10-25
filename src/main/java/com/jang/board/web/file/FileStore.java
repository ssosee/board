package com.jang.board.web.file;

import com.jang.board.domain.Photo;
import com.jang.board.web.controller.form.PostForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class FileStore {
    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public void saveFile(List<MultipartFile> file) {
        for (MultipartFile f : file) {
            try {
                f.transferTo(new File(getFullPath(f.getOriginalFilename())));
            } catch (IOException e) {
                log.error("파일 저장 실패, 파일명={}", f.getOriginalFilename());
                throw new RuntimeException(e);
            }
        }
    }
}

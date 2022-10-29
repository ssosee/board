package com.jang.board.web.file;

import com.fasterxml.jackson.core.io.CharTypes;
import com.jang.board.domain.Photo;
import com.jang.board.web.controller.form.PostForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class FileStore {
    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    //파일 저장
    public void saveFile(List<MultipartFile> file, List<String> storeFilenames) {
        StringBuffer storeFileName = new StringBuffer();
        for (int i = 0; i < storeFilenames.size(); i++) {
            storeFileName.append(storeFilenames.get(i));
            try {
                file.get(i).transferTo(new File(getFullPath(String.valueOf(storeFileName))));
                storeFileName.setLength(0); //파일명 초기화
            } catch (IOException e) {
                log.error("파일 저장 실패, 파일명={}", storeFileName);
                throw new RuntimeException(e);
            }
        }
    }

    //서버에 저장하는 파일명 가공함수
    public List<String> createStoreFileName(List<String> originalFilenames) {

        List<String> storeFilenames = new ArrayList<>();
        for (String originalFilename : originalFilenames) {
            String ext = extractExt(originalFilename);
            //서버에 저장하는 파일명
            String uuid = UUID.randomUUID().toString();//랜덤한 UUID 생성
            String storeFileName = uuid+"."+ext;
            storeFilenames.add(storeFileName);
        }

        return storeFilenames;
    }

    //확장자 추출
    private String extractExt(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos + 1);
    }

}

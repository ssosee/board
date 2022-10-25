package com.jang.board.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    private String uploadFileName;
    private String storeFileName;

    //생성 메서드
    public static Photo createPhoto(Member member, Post post, String uploadFileName) {
        Photo photo = new Photo();
        photo.changePhoto(member, post, uploadFileName);
        return photo;
    }

    public void changePhoto(Member member, Post post, String uploadFileName) {
        this.member = member;
        changePost(post);
        this.uploadFileName = uploadFileName;
        this.storeFileName = createStoreFileName(uploadFileName);
    }

    //연관관계 편의 메서드
    public void changePost(Post post) {
        this.post = post;
    }

    //서버에 저장하는 파일명 가공함수
    private String createStoreFileName(String fileName) {
        String ext = extractExt(fileName);
        //서버에 저장하는 파일명
        String uuid = UUID.randomUUID().toString();//랜덤한 UUID 생성
        String storeFileName = uuid+"."+ext;

        return storeFileName;
    }

    //확장자 추출
    private String extractExt(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos + 1);
    }

}

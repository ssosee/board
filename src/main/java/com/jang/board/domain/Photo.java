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
    public static Photo createPhoto(Member member, String uploadFileName, String storeFileName) {
        Photo photo = new Photo();
        photo.changePhoto(member, uploadFileName, storeFileName);
        return photo;
    }

    public void changePhoto(Member member, String uploadFileName, String storeFileName) {
        this.member = member;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }

    //연관관계 편의 메서드
    public void changePost(Post post) {
        this.post = post;
    }
}

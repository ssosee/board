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
        this.storeFileName = UUID.randomUUID().toString(); //랜덤한 UUID 생성
    }

    //연관관계 편의 메서드
    public void changePost(Post post) {
        this.post = post;
    }

}

package com.jang.board.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "title", "content"})
public class Post extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String title;
    @Lob
    private String content;
    @OneToMany(mappedBy = "post")
    private List<Photo> photos = new ArrayList<>();

    @Builder
    public static Post createPost(String title, String content, Member member, List<Photo> photos) {
        Post post = new Post();
        post.changePost(title, content, member);
        post.changePhoto(photos);
        return post;
    }

    private void changePost(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    //==연관관계 편의 메서드==
    private void changePhoto(List<Photo> photos) {
        this.photos = photos;
        for (Photo photo : photos) {
            photo.changePost(this);
        }
    }

    private void setPhotos(Photo photo) {
        this.photos = photos;
    }

}

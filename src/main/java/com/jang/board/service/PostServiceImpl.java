package com.jang.board.service;

import com.jang.board.domain.Member;
import com.jang.board.domain.Photo;
import com.jang.board.domain.Post;
import com.jang.board.repository.MemberRepository;
import com.jang.board.repository.PhotoRepository;
import com.jang.board.repository.PostRepository;
import com.jang.board.web.session.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PhotoRepository photoRepository;

    @Override
    @Transactional
    public Long addPost(String title, String content, List<String> uploadFileName, Long id) {

        //회원 조회
        Optional<Member> optionalMember = memberRepository.findById(id);
        Member findMember = optionalMember.orElseThrow(() -> new IllegalArgumentException("없는 회원 입니다."));

        //게시글 저장
        Post post = Post.createPost(title, content, findMember);
        postRepository.save(post);

        //사진 파일 갯수 만큼 사진 저장
        for(String fileName : uploadFileName) {
            Photo photo = Photo.createPhoto(findMember, post, fileName);
            photoRepository.save(photo);
        }

        return post.getId();
    }
}

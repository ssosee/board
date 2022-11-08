package com.jang.board.service;

import com.jang.board.domain.Member;
import com.jang.board.domain.Photo;
import com.jang.board.domain.Post;
import com.jang.board.exception.PhotoException;
import com.jang.board.exception.PostException;
import com.jang.board.repository.MemberRepository;
import com.jang.board.repository.PhotoRepository;
import com.jang.board.repository.PostRepository;
import com.jang.board.web.controller.form.SearchType;
import com.jang.board.web.file.FileStore;
import com.jang.board.web.session.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PhotoRepository photoRepository;
    private final FileStore fileStore;

    @Override
    @Transactional
    public Long addPost(String title, String content, List<String> uploadFileNames, List<String> storeFileNames, Long id) {

        //회원 조회
        Optional<Member> optionalMember = memberRepository.findById(id);
        Member findMember = optionalMember.orElseThrow(() -> new IllegalArgumentException("없는 회원 입니다."));

        //사진 파일 갯수 만큼 사진 생성
        List<Photo> photos = new ArrayList<>();
        for(int i = 0; i < uploadFileNames.size(); i++) {
            Photo photo = Photo.createPhoto(findMember, uploadFileNames.get(i), storeFileNames.get(i));
            photos.add(photo);
        }

        //게시글 생성
        Post post = Post.createPost(title, content, findMember, photos);
        postRepository.save(post);

        //사진 저장
        for (Photo photo : photos) {
            photoRepository.save(photo);
        }

        return post.getId();
    }

    @Override
    public Page<Post> findInitPosts(Pageable pageable) {
        //게시글 조회(기본값)
        Page<Post> findPosts = postRepository.findPostsBy(pageable);
        return findPosts;
    }

    @Override
    public Page<Post> findSearchPosts(Pageable pageable, String searchType, String keyword) {
        //게시글 검색 조회
        if(searchType.equals(SearchType.title.toString())) {
            return postRepository.findPostsByTitleOrContentOrAuthor(pageable, keyword, null, null);
        } else if (searchType.equals(SearchType.content.toString())) {
            return postRepository.findPostsByTitleOrContentOrAuthor(pageable, null, keyword,null);
        }
        return postRepository.findPostsByTitleOrContentOrAuthor(pageable, null, null, keyword);
    }

    @Override
    public Post findPost(Long id) {
        //게시글 단건 조회
        Optional<Post> findPost = postRepository.findById(id);
        Post post = findPost.orElseThrow(() -> new PostException("해당 게시글이 없습니다."));

        return post;
    }

    @Override
    @Transactional
    public void updatePost(Long id, String title,
                           String content,
                           List<String> originalFilenames, List<String> storeFilenames) {

        Post post = findPost(id);

        List<Photo> photos = new ArrayList<>(post.getPhotos());
        for(Photo photo : photos) {
            photoRepository.delete(photo);
        }

        photos = new ArrayList<>();
        for(int i = 0; i < originalFilenames.size(); i++) {
            Photo photo = Photo.createPhoto(post.getMember(), originalFilenames.get(i), storeFilenames.get(i));
            photos.add(photo);
            photo.changePost(post);
            photoRepository.save(photo);
        }

        post.changePost(title, content, photos);

        fileStore.deleteFile(photos.stream()
                .map(Photo::getStoreFileName)
                .collect(Collectors.toList()));

    }
}

package com.jang.board.web.controller;

import com.jang.board.domain.Post;
import com.jang.board.service.PostService;
import com.jang.board.web.controller.form.SearchPostForm;
import com.jang.board.web.controller.form.SearchTypeForm;
import com.jang.board.web.dto.PostReadDto;
import com.jang.board.web.dto.PostsDto;
import com.jang.board.web.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class PostReadController {

    private final PostService postService;
    private final FileStore fileStore;

    @GetMapping("/postList")
    public String postListForm(@PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                               @ModelAttribute("searchPostForm") SearchPostForm searchPostForm,
                               Model model) {

        Page<PostsDto> postsDtos = getPostsDtos(pageable, searchPostForm);
        model.addAttribute("postDtos", postsDtos);
        model.addAttribute("searchTypeForms", SearchTypeForm.createSearchTypeForm());
        model.addAttribute("maxPage", 5);

        return "postList";
    }

    /**
     * 조회 분류 메소드
     */
    private Page<PostsDto> getPostsDtos(Pageable pageable, SearchPostForm searchPostForm) {
        //검색 조회
        if(searchPostForm.getSearchType() != null && searchPostForm.getKeyword() != null) {
            Page<Post> searchPosts = postService.findSearchPosts(pageable, searchPostForm.getSearchType(), searchPostForm.getKeyword());
            Page<PostsDto> postsDtos = searchPosts.map(p -> new PostsDto(
                    p.getId(),
                    p.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")),
                    p.getTitle(),
                    p.getMember().getUserId()
            ));

            return postsDtos;
        }

        //기본 조회
        Page<Post> initPosts = postService.findInitPosts(pageable);
        Page<PostsDto> postsDtos =
                initPosts.map(p -> new PostsDto(
                        p.getId(),
                        p.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")),
                        p.getTitle(),
                        p.getMember().getUserId()
                ));

        return postsDtos;
    }

    @GetMapping("/postRead/{postId}")
    public String readPost(@PathVariable("postId") Long postId, Model model) {

        Post post = postService.findPost(postId);
        PostReadDto postReadDto = new PostReadDto(post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")),
                post.getTitle(),
                post.getContent(),
                post.getMember().getUserId(),
                post.getPhotos().stream()
                        .map(ph -> ph.getStoreFileName()).collect(Collectors.toList()));

        model.addAttribute("postReadDto", postReadDto);
        model.addAttribute("postId", postId);

        return "postRead";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource showImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:"+fileStore.getFullPath(filename));
    }
}

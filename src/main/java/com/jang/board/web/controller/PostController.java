package com.jang.board.web.controller;

import com.jang.board.domain.Member;
import com.jang.board.domain.Photo;
import com.jang.board.domain.Post;
import com.jang.board.repository.PostRepository;
import com.jang.board.service.PostService;
import com.jang.board.web.controller.form.PostForm;
import com.jang.board.web.controller.form.SearchPostForm;
import com.jang.board.web.controller.form.SearchTypeForm;
import com.jang.board.web.dto.PostReadDto;
import com.jang.board.web.dto.PostsDto;
import com.jang.board.web.file.FileStore;
import com.jang.board.web.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.MalformedURLException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * PRG 패턴 적용
 * Post - Redirect - Get
 * POST 방식으로 온 요청에 대해서 GET 방식의 웹페이지로 리다이렉트 시키는 패턴을 말한다.
 * 새로고침이나 뒤로 가기를 했을 경우 이전에 보내진 POST 요청이 다시 보내지는 경우 방지..!
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class PostController {

    private final PostService postService;
    private final FileStore fileStore;

    @GetMapping("/post")
    public String postForm(@ModelAttribute("postForm") PostForm postForm) {
        return "postForm";
    }

    @PostMapping("/post")
    public String post(@Valid @ModelAttribute PostForm postForm, BindingResult bindingResult, HttpServletRequest request) {

        if(bindingResult.hasErrors()) {
            return "postForm";
        }

        //로그인한 회원만 작성 가능
        HttpSession session = request.getSession(false);
        Member sessionMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (sessionMember == null) {
            return "redirect:/member/login";
        }

        //파일 원본 이름
        List<String> originalFilenames = postForm.getImageFile().stream()
                .map(f -> f.getOriginalFilename())
                .collect(Collectors.toList());

        //저장 파일 이름
        List<String> storeFilenames = fileStore.createStoreFileName(postForm.getImageFile().stream()
                .map(f -> f.getOriginalFilename())
                .collect(Collectors.toList()));

        postService.addPost(postForm.getTitle(), postForm.getContent(), originalFilenames, storeFilenames, sessionMember.getId());
        fileStore.saveFile(postForm.getImageFile(), storeFilenames); //파일 저장

        return "redirect:/member/postList";
    }

    @GetMapping("/postList")
    public String postListForm(@PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                               @ModelAttribute("searchPostForm") SearchPostForm searchPostForm,
                               Model model) {

        Page<Post> posts = postService.findPosts(pageable, null);
        Page<PostsDto> postsDtos =
                posts.map(p -> new PostsDto(
                        p.getId(), p.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")), p.getTitle(), p.getMember().getUserId()
                ));

        model.addAttribute("searchTypeForms", SearchTypeForm.createSearchTypeForm());
        model.addAttribute("postDtos", postsDtos);
        model.addAttribute("maxPage", 5);

        log.info("keyword={}", searchPostForm.getKeyword());
        log.info("searchType={}", searchPostForm.getSearchType());

        return "postList";
    }

    @GetMapping("/postRead/{postId}")
    public String readPostList(@PathVariable("postId") Long postId, Model model) {

        Optional<Post> post = postService.findPost(postId);
        Optional<PostReadDto> postReadDto = post.map(p -> new PostReadDto(p.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")),
                p.getTitle(),
                p.getContent(),
                p.getMember().getUserId(),
                p.getPhotos().stream()
                        .map(ph -> ph.getStoreFileName())
                        .collect(Collectors.toList())
                ));

        model.addAttribute("postReadDto", postReadDto.get());

        return "postRead";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource showImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:"+fileStore.getFullPath(filename));
    }
}

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.MalformedURLException;
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
    public String AddPost(@Valid @ModelAttribute PostForm postForm, BindingResult bindingResult, HttpServletRequest request) {

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

    @GetMapping("/post/{postId}")
    public String modifyPostForm(@PathVariable Long postId, Model model, HttpServletRequest request) {

        Post findPost = postService.findPost(postId);
        PostForm postForm = new PostForm(findPost.getTitle(),
                findPost.getContent(),
                findPost.getPhotos().stream()
                        .map(ph -> ph.getUploadFileName())
                        .collect(Collectors.toList()));

        model.addAttribute("postForm", postForm);

        return "postForm";
    }

    @PostMapping("/post/{postId}")
    public String modifyPost(@Valid @ModelAttribute PostForm postForm,
                             BindingResult bindingResult,
                             @PathVariable Long postId,
                             HttpServletRequest request) {

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
                .map(MultipartFile::getOriginalFilename)
                .collect(Collectors.toList());

        //저장 파일 이름
        List<String> storeFilenames = fileStore.createStoreFileName(postForm.getImageFile().stream()
                .map(MultipartFile::getOriginalFilename)
                .collect(Collectors.toList()));

        postService.updatePost(postId, postForm.getTitle(), postForm.getContent(), originalFilenames, storeFilenames);
        fileStore.saveFile(postForm.getImageFile(), storeFilenames); //파일 저장


        return "redirect:/member/postRead/{postId}";

    }
}

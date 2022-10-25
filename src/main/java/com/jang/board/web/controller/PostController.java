package com.jang.board.web.controller;

import com.jang.board.domain.Member;
import com.jang.board.domain.Post;
import com.jang.board.repository.PostRepository;
import com.jang.board.service.PostService;
import com.jang.board.web.controller.form.PostForm;
import com.jang.board.web.dto.PostsDto;
import com.jang.board.web.file.FileStore;
import com.jang.board.web.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        Member sessionMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMEBER);
        if (sessionMember == null) {
            return "redirect:/member/login";
        }

        List<String> fileName = postForm.getImageFile().stream()
                        .map(f -> f.getOriginalFilename()).collect(Collectors.toList());

        postService.addPost(postForm.getTitle(), postForm.getContent(), fileName, sessionMember.getId());
        fileStore.saveFile(postForm.getImageFile()); //파일 저장

        return "redirect:/member/postList";
    }

    @GetMapping("/postList")
    public String postListForm(@PageableDefault(direction = Sort.Direction.DESC) Pageable pageable, Model model) {

        Page<Post> posts = postService.findPosts(pageable);
        Page<PostsDto> postsDtos =
                posts.map(p -> new PostsDto(
                        p.getId(), p.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")), p.getTitle(), p.getMember().getUserId()
                ));
        model.addAttribute("postDtos", postsDtos);
        model.addAttribute("maxPage", 10);

        return "postList";
    }

    @GetMapping("/postRead")
    public String searchPostList() {
        return "postRead";
    }
}

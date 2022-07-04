package com.project.dogfaw.post.controller;


import com.project.dogfaw.post.dto.PostRequestDto;
import com.project.dogfaw.post.dto.PostResponseDto;
import com.project.dogfaw.post.service.PostService;
import com.project.dogfaw.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor

public class PostController {

    private final PostService postService;

//    post 전체조회
    @GetMapping("/api/allpost")
    public ArrayList<PostResponseDto> postPosts(@RequestHeader("Authorization") String authorization) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        String username = principal.getUser().getUsername();

        return postService.allPost(username);
    }

//    //post 생성
//    @PutMapping("/api/post")
//    public List<PostResponseDto> getPosts(){
//        return postService.getPosts();
//    }
//
//    //post 상세조회
//    @GetMapping("/")

    //post 수정
    //post 삭제


}

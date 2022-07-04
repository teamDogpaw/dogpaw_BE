package com.project.dogfaw.post.controller;


import com.project.dogfaw.post.dto.PostRequestDto;
import com.project.dogfaw.post.dto.PostResponseDto;
import com.project.dogfaw.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor

public class PostController {

    private final PostService postService;

    post 전체조회
    @GetMapping("/api/allpost")
    public PostResponseDto postPosts(@RequestBody PostRequestDto postRequestDto, @RequestHeader("Authorization") String authorization) {
        return postService.postPost(postRequestDto, authorization);
    }
    //post 생성
    @PutMapping("/api/post")
    public List<PostResponseDto> getPosts(){
        return postService.getPosts();
    }

    //post 상세조회
    @GetMapping("/")

    post 수정
    post 삭제

}

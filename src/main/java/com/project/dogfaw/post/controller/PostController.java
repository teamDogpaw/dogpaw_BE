package com.project.dogfaw.post.controller;


import com.project.dogfaw.post.dto.PostRequestDto;
import com.project.dogfaw.post.dto.PostResponseDto;
import com.project.dogfaw.post.service.PostService;
import com.project.dogfaw.security.UserDetailsImpl;
import com.project.dogfaw.user.model.User;
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
        User user = principal.getUser();

        return postService.allPost(user);
    }

    //post 생성
    @PostMapping("/api/post")
    public PostResponseDto postPosts(@RequestBody PostRequestDto postRequestDto, @RequestHeader("Authorization") String authorization) {
        return postService.postPost(postRequestDto, authorization);
    }

   //post 상세조회
    @GetMapping("/api/post/detail/{postId}")
    public PostResponseDto getPostDetail(@PathVariable Long postId){
        return postService.getPostDetail(postId);
    }


    //post 수정
    @PutMapping("/api/post")
    public Long updatePost(@PathVariable Long postId,
                           @RequestBody PostRequestDto postRequestDto,
                           @RequestHeader("Authorization") String authorization){
        postService.updatePost(postId, postRequestDto, authorization);
        return postId;
    }
    //post 삭제
    @DeleteMapping("/api/post/detail/{postId}")
    public Long deletePost(@PathVariable Long postId, @RequestHeader("Authorization") String authorization) {
        return postService.deletePost(postId, authorization)
    }

}

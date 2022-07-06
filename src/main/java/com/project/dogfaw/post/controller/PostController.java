package com.project.dogfaw.post.controller;


import com.project.dogfaw.common.CommonService;
import com.project.dogfaw.common.exception.StatusResponseDto;
import com.project.dogfaw.post.dto.PostDetailResponseDto;
import com.project.dogfaw.post.dto.PostRequestDto;
import com.project.dogfaw.post.dto.PostResponseDto;
import com.project.dogfaw.post.service.PostService;
import com.project.dogfaw.security.UserDetailsImpl;
import com.project.dogfaw.user.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;


@RestController
@RequiredArgsConstructor

public class PostController {

    private final PostService postService;
    private final CommonService commonService;

//    post 전체조회 (메인)
    @GetMapping("/api/allpost")
    public ArrayList<PostResponseDto> postPosts() {
        Long userId = commonService.getUser().getId();

        return postService.allPost(userId);
    }

    //post 생성(메인)
    @PostMapping("/api/post")
    public void postPosts(@RequestBody PostRequestDto postRequestDto) {
        User user = commonService.getUser();

        postService.postPost(postRequestDto, user);
    }

   //post 상세조회 (디테일 페이지)
    @GetMapping("/api/post/detail/{postId}")
    public PostDetailResponseDto getPostDetail(@PathVariable Long postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        String username = principal.getUser().getUsername();

        return postService.getPostDetail(postId, username);
    }


    //post 수정 (디테일 페이지)
    @PutMapping("/api/post/{postId}")
    public Long updatePost(@PathVariable Long postId,
                           @RequestBody PostRequestDto postRequestDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        String username = principal.getUser().getUsername();

        postService.updatePost(postId, postRequestDto, username);
        return postId;
    }
    //post 삭제 (디테일 페이지)
    @DeleteMapping("/api/post/{postId}")
    public Long deletePost(@PathVariable Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        String username = principal.getUser().getUsername();

        postService.deletePost(postId, username);
        return postId;
    }

}

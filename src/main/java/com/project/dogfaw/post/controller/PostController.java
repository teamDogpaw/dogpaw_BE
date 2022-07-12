package com.project.dogfaw.post.controller;


import com.amazonaws.services.dynamodbv2.xspec.L;
import com.project.dogfaw.common.CommonService;
import com.project.dogfaw.common.exception.StatusResponseDto;
import com.project.dogfaw.post.dto.BookmarkRankResponseDto;
import com.project.dogfaw.post.dto.PostDetailResponseDto;
import com.project.dogfaw.post.dto.PostRequestDto;
import com.project.dogfaw.post.dto.PostResponseDto;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.service.PostService;
import com.project.dogfaw.security.UserDetailsImpl;
import com.project.dogfaw.user.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor

public class PostController {

    private final PostService postService;
    private final CommonService commonService;

//    post 전체조회 (메인)
    @GetMapping("/api/allpost")
    public ArrayList<PostResponseDto> postPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getDetails() != null){
            User user = null;
            return postService.allPost(user);
        }else {
            UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
            User user = principal.getUser();
            return postService.allPost(user);
        }
    }

    // 북마크 랭킹 조회
    @GetMapping("/api/bookMark/rank")
    public ArrayList<PostResponseDto> bookMarkRank(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getDetails() != null){
            User user = null;
            return postService.bookMarkRank(user);
        }else {
            UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
            User user = principal.getUser();
            return postService.bookMarkRank(user);
        }
    }

    @GetMapping("/api/allposts")
    public Slice<PostResponseDto> allposts(HttpServletRequest httpServletRequest){
        int page = Integer.parseInt(httpServletRequest.getParameter("page"));
        return postService.allposts(page);
    }

    //post 생성(메인)
    @PostMapping("/api/post")
    public PostResponseDto postPosts(@RequestBody PostRequestDto postRequestDto) {
        User user = commonService.getUser();

        return postService.postPost(postRequestDto, user);
    }

   //post 상세조회 (디테일 페이지)
    @GetMapping("/api/post/detail/{postId}")
    public PostDetailResponseDto getPostDetail(@PathVariable Long postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        String username = principal.getUser().getUsername();

        return postService.getPostDetail(postId, username, postId);
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
    public ResponseEntity<StatusResponseDto> deletePost(@PathVariable Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        String username = principal.getUser().getUsername();
        postService.deletePost(postId, username);
        String data = null;
        return new ResponseEntity(new StatusResponseDto("프로필 편집이 완료되었습니다",data),HttpStatus.OK);
    }
}
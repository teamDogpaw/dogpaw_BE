package com.project.dogfaw.mypage.controller;


import com.project.dogfaw.mypage.dto.MypageRequestDto;
import com.project.dogfaw.mypage.dto.MypageResponseDto;
import com.project.dogfaw.mypage.service.MypageService;
import com.project.dogfaw.post.dto.PostResponseDto;
import com.project.dogfaw.security.UserDetailsImpl;
import com.project.dogfaw.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@RequiredArgsConstructor
@RestController
public class MypageController {

    private final MypageService mypageService;
    //북마크한 게시물 불러오기
    @GetMapping("/api/user/mypage/bookmark")
    public ArrayList<MypageResponseDto> myBookmark(@RequestHeader("Authorization") String authorization) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        User user = principal.getUser();

        return mypageService.myBookmark(user);
    }

    //내가 작성한 글 불러오기
    @GetMapping("/api/user/mypage/post")
    public ArrayList<PostResponseDto> myPost(@RequestHeader("Authorization") String authorization) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        User user = principal.getUser();

        return mypageService.myPost(user);
    }

    //내가 신청한 프로젝트 불러오기
    @GetMapping("/api/user/mypage/apply")
    public ArrayList<PostResponseDto> myApply(@RequestHeader("Authorization") String authorization) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        User user = principal.getUser();

        return mypageService.myApply(user);
    }


//    @PatchMapping("/api/user/info/{image}")
//    public ResponseEntity<Object> updateInfo(@RequestPart MultipartFile multipartFile, @RequestPart MypageRequestDto requestDto){
//
//    }

}

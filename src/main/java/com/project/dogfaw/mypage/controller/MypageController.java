package com.project.dogfaw.mypage.controller;


import com.project.dogfaw.mypage.service.MypageService;
import com.project.dogfaw.post.dto.PostResponseDto;
import com.project.dogfaw.security.UserDetailsImpl;
import com.project.dogfaw.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RequiredArgsConstructor
@RestController
public class MypageController {

    private final MypageService mypageService;

    @GetMapping("/api/user/mypage/bookmark")
    public ArrayList<PostResponseDto> myBookmark(@RequestHeader("Authorization") String authorization) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        User user = principal.getUser();

        return mypageService.myBookmark(user);
    }

    @GetMapping("/api/user/mypage/post")
    public ArrayList<PostResponseDto> myPost(@RequestHeader("Authorization") String authorization) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        User user = principal.getUser();

        return mypageService.myPost(user);
    }

    @GetMapping("/api/user/mypage/apply")
    public ArrayList<PostResponseDto> myApply(@RequestHeader("Authorization") String authorization) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        User user = principal.getUser();

        return mypageService.myApply(user);
    }

}

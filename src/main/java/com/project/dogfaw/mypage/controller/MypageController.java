package com.project.dogfaw.mypage.controller;


import com.project.dogfaw.common.CommonService;
import com.project.dogfaw.mypage.dto.MypageResponseDto;
import com.project.dogfaw.mypage.service.MypageService;
import com.project.dogfaw.post.dto.PostResponseDto;
import com.project.dogfaw.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RequiredArgsConstructor
@RestController
public class MypageController {

//    private final S3Uploader s3Uploader;
    private final CommonService commonService;
    private final MypageService mypageService;
    //북마크한 게시물 불러오기
    @GetMapping("/api/user/mypage/bookmark")
    public ArrayList<MypageResponseDto> myBookmark(@RequestHeader("Authorization") String authorization) {
        User user = commonService.getUser();

        return mypageService.myBookmark(user);
    }

    //내가 작성한 글 불러오기
    @GetMapping("/api/user/mypage/post")
    public ArrayList<PostResponseDto> myPost(@RequestHeader("Authorization") String authorization) {
        User user = commonService.getUser();

        return mypageService.myPost(user);
    }

    //내가 신청한 프로젝트 불러오기
    @GetMapping("/api/user/mypage/apply")
    public ArrayList<PostResponseDto> myApply(@RequestHeader("Authorization") String authorization) {
        User user = commonService.getUser();

        return mypageService.myApply(user);
    }


//    @PostMapping("/api/user/info/{image}")
//    public ResponseEntity<Object> updateInfo(@RequestPart MultipartFile multipartFile, @RequestPart MypageRequestDto requestDto) throws IOException {
//        User user = commonService.getUser();
//
//        s3Uploader.upload(multipartFile,"static",user,requestDto);
//
//        return new ResponseEntity<>(new StatusResponseDto("프로필이미지 수정 완료", ""), HttpStatus.OK);
//
//    }


}

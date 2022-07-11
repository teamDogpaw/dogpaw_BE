package com.project.dogfaw.mypage.controller;


import com.project.dogfaw.common.CommonService;
import com.project.dogfaw.common.exception.CustomException;
import com.project.dogfaw.common.exception.ErrorCode;
import com.project.dogfaw.common.exception.StatusResponseDto;
import com.project.dogfaw.mypage.dto.MypageRequestDto;
import com.project.dogfaw.mypage.dto.MypageResponseDto;
import com.project.dogfaw.mypage.img.S3Uploader;
import com.project.dogfaw.mypage.service.MypageService;
import com.project.dogfaw.post.dto.PostResponseDto;
import com.project.dogfaw.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;

@RequiredArgsConstructor
@RestController
public class MypageController {

    private final S3Uploader s3Uploader;
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

    //프로필 이미지를 넣지 않고 요청할 시 프론트에서 설정한 기본이미지로 바뀌어야함
    @Transactional
    @PostMapping ("/api/user/modify")
    public ResponseEntity<Object> updateInfo(
            @RequestPart(required = false) MultipartFile multipartFile,
            @RequestPart MypageRequestDto requestDto) throws IOException {

        User user = commonService.getUser();
        String data = null;
//        try {
            s3Uploader.uploadFiles(multipartFile, "static",requestDto,user);
//        } catch (Exception e) { return new ResponseEntity<>(new StatusResponseDto("프로필이미지가 누락되었습니다.",data), HttpStatus.BAD_REQUEST);}
        return new ResponseEntity(new StatusResponseDto("프로필 편집이 완료되었습니다",data), HttpStatus.OK);
    }
}

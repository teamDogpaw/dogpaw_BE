package com.project.dogfaw.mypage.controller;


import com.project.dogfaw.common.CommonService;
import com.project.dogfaw.common.exception.StatusResponseDto;
import com.project.dogfaw.mypage.dto.AllApplicantsDto;
import com.project.dogfaw.mypage.dto.MypageRequestDto;
import com.project.dogfaw.mypage.dto.MypageResponseDto;
import com.project.dogfaw.mypage.service.S3Uploader;
import com.project.dogfaw.mypage.service.MypageService;
import com.project.dogfaw.post.dto.PostResponseDto;
import com.project.dogfaw.user.model.User;
import jdk.nashorn.internal.runtime.Undefined;
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


    /*북마크한 게시물 불러오기*/
    @GetMapping("/api/user/mypage/bookmark")
    public ArrayList<MypageResponseDto> myBookmark() {
        User user = commonService.getUser();
        return mypageService.myBookmark(user);
    }


    /*내가 작성한 글 불러오기*/
    @GetMapping("/api/user/mypage/post")
    public ArrayList<PostResponseDto> myPost() {
        User user = commonService.getUser();
        return mypageService.myPost(user);
    }


    /*내가 지원한 프로젝트 불러오기*/
    @GetMapping("/api/user/mypage/apply")
    public ArrayList<PostResponseDto> myApply() {
        User user = commonService.getUser();
        return mypageService.myApply(user);
    }


    /*내가 참여한 프로젝트 불러오기*/
    @GetMapping("/api/user/participation")
    public ArrayList<PostResponseDto> participation(){
        User user = commonService.getUser();
        return mypageService.participation(user);
    }


    /*지원자 보기(모집글 작성자만)*/
    @GetMapping("/api/allApplicants/info/{postId}")
    public ArrayList<AllApplicantsDto> allApplicants(@PathVariable Long postId){
        User user = commonService.getUser();
        return mypageService.allApplicants(postId,user);
    }


    /*프로필 이미지를 넣지 않고 요청할 시 프론트에서 설정한 기본이미지로 바뀌어야함*/
    @Transactional
    @PutMapping ("/api/user/info")
    public ResponseEntity<Object> updateInfo(
            @RequestPart("image") MultipartFile multipartFile,
            @RequestPart("body") MypageRequestDto requestDto) throws IOException {
        User user = commonService.getUser();

        if (multipartFile!=null){
            s3Uploader.uploadFiles(multipartFile, "static",requestDto,user);

        }else {
            mypageService.updateProfile(requestDto,user);
        }
        return new ResponseEntity(new StatusResponseDto("프로필 편집이 완료되었습니다",""), HttpStatus.OK);
    }


    /*유저 프로필 이미지 기본이미지로 변경 요청*/
    @PutMapping ("/api/user/profile/basic")
    public ResponseEntity<Object> updateInfo(){
        User user = commonService.getUser();
        mypageService.basicImg(user);
        return new ResponseEntity(new StatusResponseDto("프로필 편집이 완료되었습니다",""), HttpStatus.OK);
    }
}

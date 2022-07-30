package com.project.dogfaw.mypage.controller;


import com.project.dogfaw.common.CommonService;
import com.project.dogfaw.common.exception.StatusResponseDto;
import com.project.dogfaw.mypage.dto.*;
import com.project.dogfaw.mypage.service.MypageService;
import com.project.dogfaw.mypage.service.S3Uploader;
import com.project.dogfaw.post.dto.MyApplyingResponseDto;
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


    /*북마크한 게시물 불러오기*/
    @GetMapping("/api/user/mypage/bookmark")
    public ArrayList<MyBookmarkResponseDto> myBookmark() {
        User user = commonService.getUser();
        return mypageService.myBookmark(user);
    }


    /*내가 작성한 글 불러오기*/
    @GetMapping("/api/user/mypage/post")
    public ArrayList<MyPostResponseDto> myPost() {
        User user = commonService.getUser();
        return mypageService.myPost(user);
    }


    /*내가 지원한 프로젝트 불러오기*/
    @GetMapping("/api/user/mypage/apply")
    public ArrayList<MyApplyingResponseDto> myApply() {
        User user = commonService.getUser();
        return mypageService.myApply(user);
    }


    /*내가 참여한 프로젝트 불러오기*/
    @GetMapping("/api/user/participation")
    public ArrayList<MyAcceptanceResponseDto> participation(){
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
            @RequestPart(value = "image",required = false) MultipartFile multipartFile,
            @RequestPart("body") MypageRequestDto requestDto) throws IOException {
        User user = commonService.getUser();

        if (multipartFile != null){
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

    /*내 팀원보기*/
    @GetMapping("/api/user/team/{postId}")
    public ArrayList<AllTeammateDto> checkTeammate(@PathVariable Long postId){
        User user = commonService.getUser();
        return mypageService.checkTeammate(postId);
    }

    /*팀원 추방하기*/
    @DeleteMapping("/api/expulsion/{userId}/teammate/{postId}")
    public ResponseEntity<Object> expulsionTeammate(@PathVariable Long userId, @PathVariable Long postId){
        User user = commonService.getUser();
        return mypageService.expulsionTeammate(userId,postId,user);
    }

    /*참가자 자진 팀 탈퇴*/
    @DeleteMapping("/api/withdraw/team/{postId}")
    public ResponseEntity<Object> withdrawTeam(@PathVariable Long postId) {
        User user = commonService.getUser();
        return mypageService.withdrawTeam(postId, user);
    }

    /*다른유저 마이페이지 보기(프로필,참여한 프로젝트, 모집중인 프로젝트)*/
    @GetMapping("/api/user/{nickname}/other/mypage/info")
    public OtherUserMypageResponseDto mypageInfo(@PathVariable String nickname){
        User user = commonService.getUser();
        return mypageService.mypageInfo(nickname,user);
    }
}

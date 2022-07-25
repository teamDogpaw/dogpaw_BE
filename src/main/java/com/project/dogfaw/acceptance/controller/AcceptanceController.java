package com.project.dogfaw.acceptance.controller;

import com.project.dogfaw.acceptance.service.AcceptanceService;
import com.project.dogfaw.common.CommonService;
import com.project.dogfaw.common.exception.StatusResponseDto;
import com.project.dogfaw.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AcceptanceController {

    private final AcceptanceService acceptanceService;
    private final CommonService commonService;

    /* 참여신청수락(작성자만)*/
    @PostMapping("/api/applicant/{userId}/acceptance/{postId}")
    public ResponseEntity<Object> acceptance(@PathVariable Long userId, @PathVariable Long postId){
        User user = commonService.getUser();
        acceptanceService.acceptance(userId,postId,user);
        return new ResponseEntity(new StatusResponseDto("수락이완료되었습니다",""), HttpStatus.OK);
    }


    /*참여신청거절(작성자만)*/
    @DeleteMapping("/api/applicant/{userId}/rejection/{postId}")
    public ResponseEntity<Object> rejection(@PathVariable Long userId, @PathVariable Long postId){
        User user = commonService.getUser();
        acceptanceService.rejection(userId,postId,user);
        return new ResponseEntity(new StatusResponseDto("거절이완료되었습니다",""),HttpStatus.OK);
    }

}

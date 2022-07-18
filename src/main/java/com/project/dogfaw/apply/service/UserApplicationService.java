package com.project.dogfaw.apply.service;


import com.project.dogfaw.apply.model.UserApplication;
import com.project.dogfaw.apply.repository.UserApplicationRepository;
import com.project.dogfaw.common.exception.CustomException;
import com.project.dogfaw.common.exception.ErrorCode;
import com.project.dogfaw.common.exception.StatusResponseDto;
import com.project.dogfaw.common.validator.ApplyValidator;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.repository.PostRepository;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;



@RequiredArgsConstructor
@Service
public class UserApplicationService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserApplicationRepository userApplicationRepository;

    @Transactional
    public ResponseEntity<Object> userApply(Long postId, User user) {

        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new CustomException(ErrorCode.POST_NOT_FOUND)
        );

        //DB를 조회하여 참여신청 이력이 없으면 DB에 저장후 해당 게시글의 현재 모집인원 +1
        //참여 취소 버튼 클릭 시 DB를 조회하여 참여신청 이력이 있을경우 DB에서 삭제후 현재 모집인원 -1
        if(!userApplicationRepository.existsByUserAndPost(user,post)){
            //모집인원 마감 체크
            ApplyValidator.currentMemberCheck(post);

            UserApplication userApplication = new UserApplication(user,post);
            userApplicationRepository.save(userApplication);
            Boolean data = true;
            return new  ResponseEntity<>(new StatusResponseDto("신청이 완료되었습니다.", data), HttpStatus.OK);
        }else{
            UserApplication userApplication = userApplicationRepository.getUserApplicationByUserAndPost(user,post);
            userApplicationRepository.delete(userApplication);
            Boolean data = false;
            return new  ResponseEntity<>(new StatusResponseDto("신청이 취소되었습니다.", data), HttpStatus.OK);
        }

    }


}

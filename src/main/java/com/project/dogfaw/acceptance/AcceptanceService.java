package com.project.dogfaw.acceptance;

import com.project.dogfaw.apply.model.UserApplication;
import com.project.dogfaw.apply.repository.UserApplicationRepository;
import com.project.dogfaw.common.exception.CustomException;
import com.project.dogfaw.common.exception.ErrorCode;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.repository.PostRepository;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class AcceptanceService {
    private final PostRepository postRepository;
    private final UserApplicationRepository userApplicationRepository;
    private final AcceptanceRepository acceptanceRepository;
    private final UserRepository userRepository;

    @Transactional
    /*신청수락*/
    public void acceptance(Long userId, Long postId,User user) {
        User applier = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(ErrorCode.SIGNUP_USERID_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new CustomException(ErrorCode.POST_NOT_FOUND));
        Long writer = post.getUser().getId();
        Acceptance acceptance = new Acceptance(applier,post);

        //작성자확인 후 수락(저장), 시청상태 삭제, 현재인원+1
        if(!writer.equals(user.getId())){
            throw new CustomException(ErrorCode.MYPAGE_REJECTION_NO_AUTHORITY);
        }

        userApplicationRepository.deleteByUserAndPost(applier,post)
                .orElseThrow(()-> new CustomException(ErrorCode.APPLY_NOT_FOUND));

        acceptanceRepository.save(acceptance);
        post.increaseCnt(); //post.decreaseCnt = 수락된 신청자가 상세페이지에서 참여취소 하였을 때 acceptanceRepo에서 삭제하고 -1 해야함

    }
    @Transactional
    /*신청거절하기*/
    public void rejection(Long userId, Long postId, User user) {
        User applier = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(ErrorCode.SIGNUP_USERID_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new CustomException(ErrorCode.POST_NOT_FOUND));
        Long writer = post.getUser().getId();


        //작성자확인 후 거절(신청상태삭제)
        if(!writer.equals(user.getId())){
            throw new CustomException(ErrorCode.MYPAGE_REJECTION_NO_AUTHORITY);
        }

        userApplicationRepository.deleteByUserAndPost(applier,post)
                .orElseThrow(()-> new CustomException(ErrorCode.APPLY_NOT_FOUND));

    }
}

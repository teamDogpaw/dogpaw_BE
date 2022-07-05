package com.project.dogfaw.apply.service;


import com.project.dogfaw.apply.model.UserApplication;
import com.project.dogfaw.apply.repository.UserApplicationRepository;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.repository.PostRepository;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;



@RequiredArgsConstructor
@Service
public class UserApplicationService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserApplicationRepository userApplicationRepository;

    @Transactional
    public Long userApply(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new NullPointerException("해당 ID가 존재하지 않음")
        );
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new NullPointerException("해당 게시물이 존재하지 않음")
        );

        //해당 게시물의 현재 모집인원 수 와 쵀대 모집인원 수
        int currentCnt = post.getCurrentMember();
        int maxCnt = post.getMaxCapacity();

        //인원 꽉찰 경우 모집마감(deadline = true)
        if(currentCnt==maxCnt){
            post.isDeadline();
            return 1L;
        } else{
            post.isOngoing();
        }

        //DB를 조회하여 참여신청 이력이 없으면 DB에 저장후 해당 게시글의 현재 모집인원 +1
        //참여 취소 버튼 클릭 시 DB를 조회하여 참여신청 이력이 있을경우 DB에서 삭제후 현재 모집인원 -1
        if(!userApplicationRepository.existsByUserAndPost(user,post)){
            UserApplication userApplication = new UserApplication(user,post);
            userApplicationRepository.save(userApplication);
            post.increaseCnt();
            return 2L;
        }else{
            UserApplication userApplication = userApplicationRepository.getUserApplicationByUserAndPost(user,post);
            userApplicationRepository.delete(userApplication);
            post.decreaseCnt();
            return 3L;
        }

    }
}

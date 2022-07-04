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
    public boolean userApply(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new NullPointerException("해당 ID가 존재하지 않음")
        );
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new NullPointerException("해당 게시물이 존재하지 않음")
        );

        //해당 게시물의 현재 모집인원 수 와 쵀대 모집인원 수
        int currentCnt = post.getCurrentMember();
        int maxCmt = post.getMaxCapacity();
//        int plus = 1;
//        int sum = currentCnt + plus;
        //현재 모집 인원체크
        if(currentCnt==maxCmt){throw new IllegalArgumentException("모집인원이 마감되었습니다");}

        //db에 존재하지 않을경우
        if(!userApplicationRepository.existsByUserAndPost(user,post)){
            UserApplication userApplication = new UserApplication(user,post);
            UserApplicationRepository.save(userApplication);
//            post.setCurrentMember(sum);
            post.increaseCnt();
            return true;
        }else{
            UserApplication userApplication = userApplicationRepository.getUserApplicationByUserAndPost(user,post);
            userRepository.delete(userApplication);
            return false;
        }

    }
}

/*방법2*/
// if(userApplicationRepository.findByUserAndPost(user,post)==null){
//         UserApplication userApplication = new UserApplication(user,post);
//         UserApplicationRepository.save(userApplication);
//            post.setCurrentMember(sum);
//         post.increaseCnt();
//         return true;
//         }else{
//         UserApplication userApplication = userApplicationRepository.getUserApplicationByUserAndPost(user,post);
//         userRepository.delete(userApplication);
//         return false;
//         }

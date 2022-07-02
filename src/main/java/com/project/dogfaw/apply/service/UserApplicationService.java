package com.project.dogfaw.apply.service;


import com.project.dogfaw.apply.model.UserApplication;
import com.project.dogfaw.apply.repository.UserApplicationRepository;
import com.project.dogfaw.bookmark.model.BookMark;
import com.project.dogfaw.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;



@RequiredArgsConstructor
@Service
public class UserApplicationService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final UserApplicationRepository userApplicationRepository;

    @Transactional
    public boolean userApply(Long postId, Long userId) {
        Member member = memberRepository.findById(userId).orElseThrow(
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

        if(UserApplicationRepository.findByMemberAndPost(member,post))==null){
            UserApplication userApplication = new UserApplication(,post);
            UserApplicationRepository.save(userApplication);
            post.setCurrentMember(sum);
            return true;
        }else{
            UserApplication userApplication = userApplicationRepository.getUserApplicationByUserAndPost(user,post);
            userRepository.delete(userApplication);
            return false;
        }

    }
}

package com.project.dogfaw.acceptance.service;

import com.project.dogfaw.acceptance.repository.AcceptanceRepository;
import com.project.dogfaw.acceptance.model.Acceptance;
import com.project.dogfaw.apply.repository.UserApplicationRepository;
import com.project.dogfaw.common.exception.CustomException;
import com.project.dogfaw.common.exception.ErrorCode;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.repository.PostRepository;
import com.project.dogfaw.sse.model.NotificationType;
import com.project.dogfaw.sse.service.NotificationService;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

//지원자한테 알람이 가야함
@RequiredArgsConstructor
@Service
public class AcceptanceService {
    private final PostRepository postRepository;
    private final UserApplicationRepository userApplicationRepository;
    private final AcceptanceRepository acceptanceRepository;
    private final UserRepository userRepository;

    private final NotificationService notificationService;

    @Transactional
    /*신청수락*/
    public void acceptance(Long userId, Long postId,User user) {
        //지원자
        User applier = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(ErrorCode.SIGNUP_USERID_NOT_FOUND));
        //게시글
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new CustomException(ErrorCode.POST_NOT_FOUND));
        //게시글 작성자
        Long writer = post.getUser().getId();
        //지원자+게시글
        Acceptance acceptance = new Acceptance(applier,post);
        //해당유저의게시글과포스트찾기
//        UserApplication userApply = userApplicationRepository.findUserApplyByUserAndPost(applier, post).orElseThrow(
//                () -> new CustomException(ErrorCode.APPLY_NOT_FOUND)
//        );

        //작성자확인
        if(!writer.equals(user.getId())){
            throw new CustomException(ErrorCode.MYPAGE_REJECTION_NO_AUTHORITY);
        }
        //신청자확인
        userApplicationRepository.findByUserAndPost(applier,post)
                .orElseThrow(()-> new CustomException(ErrorCode.APPLY_NOT_FOUND));
        //수락완료 저장 전 현재모집인원 체크
        if(post.getCurrentMember()>=post.getMaxCapacity()){
            throw new CustomException(ErrorCode.ACCEPTANCE_PEOPLE_SET_CLOSED);
        }
        //신청상태 삭제
        userApplicationRepository.deleteByUserAndPost(applier,post)
                .orElseThrow(()-> new CustomException(ErrorCode.APPLY_NOT_FOUND));

        //수락상태로 저장
        acceptanceRepository.save(acceptance);

        // 알림
        //해당 댓글로 이동하는 url
        String Url = "https://dogpaw.kr/detail/"+postId;
//신청 수락 시 신청 유저에게 실시간 알림 전송 ,
        String content = applier.getNickname()+"님! 프로젝트 매칭 알림이 도착했어요!";
        notificationService.send(applier,NotificationType.ACCEPT,content,Url);

        //현재인원+1
        post.increaseCnt(); //post.decreaseCnt = 수락된 신청자가 상세페이지에서 참여취소 하였을 때 acceptanceRepo에서 삭제하고 -1 해야함
        //모집정원이 모두 찼을 경우 모집마감
        if(post.getCurrentMember()==post.getMaxCapacity()){
            Boolean deadline = true;
            post.updateDeadline(deadline);
        }


    }
    @Transactional
    /*신청거절하기*/
    public void rejection(Long userId, Long postId, User user) {
        //신청자
        User applier = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(ErrorCode.SIGNUP_USERID_NOT_FOUND));
        //게시글
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new CustomException(ErrorCode.POST_NOT_FOUND));
        //작성자
        Long writer = post.getUser().getId();

        //해당유저의게시글과포스트찾기
//        UserApplication userApply = userApplicationRepository.findUserApplyByUserAndPost(applier, post).orElseThrow(
//                () -> new CustomException(ErrorCode.APPLY_NOT_FOUND)
//        );

        //작성자확인
        if(!writer.equals(user.getId())){
            throw new CustomException(ErrorCode.MYPAGE_REJECTION_NO_AUTHORITY);
        }
        //신청자확인
        userApplicationRepository.findByUserAndPost(applier,post)
                .orElseThrow(()-> new CustomException(ErrorCode.APPLY_NOT_FOUND));
        //신청거절 = 신청상태 삭제
        userApplicationRepository.deleteByUserAndPost(applier,post);

        //프로젝트 매칭 실패 알림
        //해당 댓글로 이동하는 url
        String Url = "https://dogpaw.kr/detail/"+postId;
        String notificationContent = applier.getNickname()+"님! 프로젝트 매칭 실패 알림이 도착했어요!";
        notificationService.send(applier,NotificationType.REJECT,notificationContent,Url);


    }
}

package com.project.dogfaw.apply.service;

//작성자한테 알람이 가야함
import com.project.dogfaw.acceptance.repository.AcceptanceRepository;
import com.project.dogfaw.apply.model.UserApplication;
import com.project.dogfaw.apply.repository.UserApplicationRepository;
import com.project.dogfaw.common.exception.CustomException;
import com.project.dogfaw.common.exception.ErrorCode;
import com.project.dogfaw.common.exception.StatusResponseDto;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.repository.PostRepository;
import com.project.dogfaw.sse.model.NotificationType;
import com.project.dogfaw.sse.service.NotificationService;
import com.project.dogfaw.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;



@RequiredArgsConstructor
@Service
public class UserApplicationService {

    private final PostRepository postRepository;
    private final AcceptanceRepository acceptanceRepository;
    private final UserApplicationRepository userApplicationRepository;

    private final NotificationService notificationService;

    @Transactional
    public ResponseEntity<Object> userApply(Long postId, User user) {

        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new CustomException(ErrorCode.POST_NOT_FOUND)
        );


        //DB를 조회하여 참여신청 이력이 없으면 DB에 저장후 해당 게시글의 현재 모집인원 +1
        //참여 취소 버튼 클릭 시 DB를 조회하여 참여신청 이력이 있을경우 DB에서 삭제후 현재 모집인원 -1

        //지원할때 만약 해당 유저가 수락된 유저일경우 다시 지원 할 수 없게 해야함
        if(acceptanceRepository.existsByUserAndPost(user,post)){
            throw new CustomException(ErrorCode.ALREADY_APPLY_POST_ERROR);
        }
        //지원하기 신청/취소
        if(!userApplicationRepository.existsByUserAndPost(user,post)){
            UserApplication userApplication = new UserApplication(user,post);
            userApplicationRepository.save(userApplication);

            Boolean data = true;

            // 알림
            // '모집글' -> '신청' 시에 모집글 작성자에게 실시간 알림을 보낸다.
            //해당 댓글로 이동하는 url
            String Url = "https://dogpaw.kr/detail/"+post.getId();
            //신청 시 모집글 작성 유저에게 실시간 알림 전송 ,
            String notificationContent = user.getNickname()+"님이 프로젝트를 신청하셨습니다!";
            notificationService.send(post.getUser(),NotificationType.APPLY,notificationContent,Url);


            return new  ResponseEntity<>(new StatusResponseDto("신청이 완료되었습니다.", data), HttpStatus.OK);

        }else{
            UserApplication userApplication = userApplicationRepository.getUserApplicationByUserAndPost(user,post);
            userApplicationRepository.delete(userApplication);
            Boolean data = false;
            return new  ResponseEntity<>(new StatusResponseDto("신청이 취소되었습니다.", data), HttpStatus.OK);
        }

    }


}

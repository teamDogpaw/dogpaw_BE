package com.project.dogfaw.sse.controller;

import com.project.dogfaw.common.CommonService;
import com.project.dogfaw.sse.security.UserDetailsImpl;
import com.project.dogfaw.sse.dto.NotificationCountDto;
import com.project.dogfaw.sse.dto.NotificationDto;
import com.project.dogfaw.sse.service.NotificationService;
import com.project.dogfaw.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {


    private final NotificationService notificationService;

    private final CommonService commonService;

    // MIME TYPE - text/event-stream 형태로 받아야함. EventStream의 생성은 최초 클라이언트 요청으로 발생한다. EventStream이 생성되면 서버는 원하는 시점에 n개의 EventStream에 Event 데이터를 전송할 수 있다.
    // 클라이어트로부터 오는 알림 구독 요청을 받는다.
    // 로그인한 유저는 SSE 연결
    // lAST_EVENT_ID = 이전에 받지 못한 이벤트가 존재하는 경우 [ SSE 시간 만료 혹은 종료 ]
    // 전달받은 마지막 ID 값을 넘겨 그 이후의 데이터[ 받지 못한 데이터 ]부터 받을 수 있게 한다
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(HttpServletResponse response, @AuthenticationPrincipal UserDetailsImpl userDetails,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "")
                                String lastEventId) {

        //추가
        response.setCharacterEncoding("UTF-8");

        Long userId = commonService.getUser().getId();
        
        return notificationService.subscribe(userId, lastEventId);

    }

    //알림조회
    @GetMapping(value = "/notifications")
    public List<NotificationDto> findAllNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = commonService.getUser().getId();
        return notificationService.findAllNotifications(userId);
    }

    //전체목록 알림 조회에서 해당 목록 클릭 시 읽음처리 ,
    @PostMapping("/notification/read/{notificationId}")
    public void readNotification(@PathVariable Long notificationId) {
        notificationService.readNotification(notificationId);

    }

    //알림 조회 - 구독자가 현재 읽지않은 알림 갯수
    @GetMapping(value = "/notifications/count")
    public NotificationCountDto countUnReadNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = commonService.getUser().getId();
        return notificationService.countUnReadNotifications(userId);
    }

    //알림 전체 삭제
    @DeleteMapping(value = "/notifications/delete")
    public ResponseEntity<Object> deleteNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = commonService.getUser();

        return notificationService.deleteAllByNotifications(user);

    }

    //단일 알림 삭제
    @DeleteMapping(value = "/notifications/delete/{notificationId}")
    public ResponseEntity<Object> deleteNotification(@PathVariable Long notificationId) {

        return notificationService.deleteByNotifications(notificationId);
    }

}
    /*
        1. count -> 안 읽은 카운트
        2. reset -> 전체목록은 전체목록만 , 읽음처리를 할 수 있는 api가 필요함


        0. 구독 -> 서버로부터 오는 알람 받음

        2.notifications -> 내가 가지고있는 알림 목록을 다불러옴 [불러올 때 애들의 스테이터스 상태는 true가 됨]

        1.notifications/count -> notifications 알람에서 상태가 false 인 친구들을 가져옴
     */


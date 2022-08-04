package com.project.dogfaw.post.controller;


import com.project.dogfaw.common.CommonService;
import com.project.dogfaw.common.exception.StatusResponseDto;
import com.project.dogfaw.post.dto.PostDetailResponseDto;
import com.project.dogfaw.post.dto.PostRequestDto;
import com.project.dogfaw.post.dto.MyApplyingResponseDto;
import com.project.dogfaw.post.service.PostService;
import com.project.dogfaw.sse.security.UserDetailsImpl;
import com.project.dogfaw.user.model.User;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final CommonService commonService;
    private HikariDataSource dataSource;

    /*post 전체조회 (메인)*/
    @GetMapping("/api/all/post")
    public Map<String, Object> postPosts(HttpServletRequest httpServletRequest) {
        // 팀원 외에 다른 ip에서 요청이 들어오는지 확인 위함
        log.info("===========================요청한 ip"+getClientIpAddr(httpServletRequest)+"=====================================================");

        //hikariStatus확인용
        printHikariStatus();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getDetails() != null){
            User user = null;
            long page = Long.parseLong(httpServletRequest.getParameter("page"));
            return postService.allPost(user,page);
        }else {
            UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
            User user = principal.getUser();
            long page = Long.parseLong(httpServletRequest.getParameter("page"));
            return postService.allPost(user,page);
        }
    }


    /*북마크 랭킹 조회*/
    @GetMapping("/api/bookMark/rank")
    public ArrayList<MyApplyingResponseDto> bookMarkRank(HttpServletRequest httpServletRequest){
        // 팀원 외에 다른 ip에서 요청이 들어오는지 확인 위함
        log.info("===========================요청한 ip"+getClientIpAddr(httpServletRequest)+"=====================================================");

        //hikariStatus확인용
        printHikariStatus();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getDetails() != null){
            User user = null;
            return postService.bookMarkRank(user);
        }else {
            UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
            User user = principal.getUser();
            return postService.bookMarkRank(user);
        }
    }


    /*post 생성(메인)*/
    @PostMapping("/api/post")
    public MyApplyingResponseDto postPosts(@RequestBody PostRequestDto postRequestDto) {

        //hikariStatus확인용
        printHikariStatus();

        User user = commonService.getUser();
        return postService.postPost(postRequestDto, user);
    }


    /*post 상세조회 (디테일 페이지)*/
    @GetMapping("/api/post/detail/{postId}")
    public PostDetailResponseDto getPostDetail(@PathVariable Long postId){

        //hikariStatus확인용
        printHikariStatus();

        //로그인한 유저와 안한유저 구분
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getDetails() != null){
            User user = null;
            return postService.getPostDetail(postId,user);
        }else {
            UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
            User user = principal.getUser();
            return postService.getPostDetail(postId,user);
        }
    }


    /*post 수정 (디테일 페이지)*/
    @PutMapping("/api/post/{postId}")
    public Long updatePost(@PathVariable Long postId,
                           @RequestBody PostRequestDto postRequestDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        String username = principal.getUser().getUsername();

        postService.updatePost(postId, postRequestDto, username);
        return postId;
    }


    /*post 삭제 (디테일 페이지)*/
    @DeleteMapping("/api/post/{postId}")
    public ResponseEntity<StatusResponseDto> deletePost(@PathVariable Long postId) {
        User user = commonService.getUser();
        postService.deletePost(postId, user);
        String data = null;
        return new ResponseEntity(new StatusResponseDto("게시글 삭제가 완료되었습니다",data),HttpStatus.OK);
    }

    /*모집마감,모집마감 취소(작성자만)*/
    @PostMapping("/api/post/{postId}/deadline")
    public ResponseEntity<Object> updateDeadline(@PathVariable Long postId){
        User user = commonService.getUser();
        return postService.updateDeadline(postId,user);
    }


    /*요청에 대한 ip추적*/
    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }


    @Autowired
    private HikariPoolMXBean poolMXBean;


    private void printHikariStatus(){
        log.info("connections info total: {}, active: {}, idle: {}, await: {}", poolMXBean.getTotalConnections(),
                poolMXBean.getActiveConnections(), poolMXBean.getIdleConnections(), poolMXBean.getThreadsAwaitingConnection());
    }

}
package com.project.dogfaw.comment.controller;

import com.project.dogfaw.comment.dto.CommentPutDto;
import com.project.dogfaw.comment.dto.CommentRequestDto;
import com.project.dogfaw.comment.dto.CommentResponseDto;
import com.project.dogfaw.comment.model.Comment;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.comment.service.CommentService;
import com.project.dogfaw.common.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;
    private final CommonService commonService;


    // 댓글 생성
    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<Void> registComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto) {
        //@AuthenticationPrincipal은 null로 받아온다. Authentication으로 받아오기.
        User user = commonService.getUser();
        // 세션 가져오기...
        commentService.saveNewComments(postId, user, requestDto);
        return ResponseEntity.ok().build();
    }

    // 댓글 조회
    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok().body(commentService.getCommentsByPostId(postId));
    }

    // 댓글 삭제
    @DeleteMapping("/api/posts/{postId}/comments/{commentId}")
    public Boolean deleteComment(@PathVariable Long commentId, @PathVariable Long postId) {
        //@AuthenticationPrincipal은 null로 받아온다. Authentication으로 받아오기.
        User user = commonService.getUser();
        //사용자의 id가 null값인지 검증

        return commentService.deleteComment(commentId, user, postId);
    }

    // 댓글 수정
    @PutMapping("/api/posts/{postId}/comments/{commentId}")
    public Boolean updateComment(@PathVariable Long commentId, @RequestBody CommentPutDto requestDto) {
        //@AuthenticationPrincipal은 null로 받아온다. Authentication으로 받아오기.
        User user = commonService.getUser();

        return commentService.updateComment(commentId, user, requestDto);
    }
}
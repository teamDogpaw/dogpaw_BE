package com.project.dogfaw.comment.controller;

import com.project.dogfaw.comment.dto.CommentPutDto;
import com.project.dogfaw.comment.dto.CommentRequestDto;
import com.project.dogfaw.comment.service.CommentService;
import com.project.dogfaw.common.CommonService;
import com.project.dogfaw.common.exception.StatusResponseDto;
import com.project.dogfaw.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    private final CommonService commonService;


    // 댓글 생성
    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<Void> registComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto) {
        User user = commonService.getUser();

        commentService.saveNewComments(postId, user, requestDto);

        return ResponseEntity.ok().build();
    }

    // 댓글 조회
    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<StatusResponseDto> getCommentsByPostId(@PathVariable Long postId) {

        return ResponseEntity.ok().body(commentService.getCommentsByPostId(postId));
    }

    // 댓글 삭제
    @DeleteMapping("/api/posts/{postId}/comments/{commentId}")
    public Boolean deleteComment(@PathVariable Long commentId, @PathVariable Long postId) {
        User user = commonService.getUser();

        return commentService.deleteComment(commentId, user, postId);
    }

    // 댓글 수정
    @PutMapping("/api/posts/{postId}/comments/{commentId}")
    public Boolean updateComment(@PathVariable Long commentId, @RequestBody CommentPutDto requestDto) {
        User user = commonService.getUser();

        return commentService.updateComment(commentId, user, requestDto);
    }
}
package com.project.dogfaw.comment.controller;

import com.project.dogfaw.comment.dto.CmtReplyPutDto;
import com.project.dogfaw.comment.dto.CmtReplyReqeustDto;
import com.project.dogfaw.comment.dto.CmtReplyResponseDto;
import com.project.dogfaw.comment.service.CommentReplyService;
import com.project.dogfaw.common.CommonService;
import com.project.dogfaw.common.exception.StatusResponseDto;
import com.project.dogfaw.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController

public class CommentReplyController {

    private final CommentReplyService commentReplyService;

    private final CommonService commonService;


    //대댓글생성
    @PostMapping("/api/comments/{commentId}/commentReply")
    public ResponseEntity<Void> registCommentReply(@PathVariable Long commentId, @RequestBody CmtReplyReqeustDto requestDto) {
        User user = commonService.getUser();
        commentReplyService.saveCmtReply(commentId, user, requestDto);
        return ResponseEntity.ok().build();
    }

    //대댓글수정
    @PutMapping("/api/comments/{commentId}/{commentReplyId}")
    public Boolean updateCommentReply(@PathVariable Long commentReplyId, @RequestBody CmtReplyPutDto requestDto) {
        User user = commonService.getUser();

        return commentReplyService.updateCommentReply(commentReplyId, user, requestDto);
    }


    //대댓글삭제
    @DeleteMapping("/api/comments/{commentId}/{commentReplyId}")
    public Boolean deleteCommentReply(@PathVariable Long commentId, @PathVariable Long commentReplyId) {
        User user = commonService.getUser();

        return commentReplyService.deleteCommentReply(commentReplyId, user, commentId);
    }
}

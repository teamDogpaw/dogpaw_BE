package com.project.dogfaw.comment.dto;

import com.project.dogfaw.comment.model.Comment;
import com.project.dogfaw.comment.model.CommentReply;
import com.project.dogfaw.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private Long commentId;
    private String content;

    private String modifiedAt;
    private Long userId;
    private String nickname;

    private String profileImg;

    private List<CmtReplyResponseDto> commentReplyList;


    public CommentResponseDto(Comment comment,List<CmtReplyResponseDto> cmtReplyResponseDtoList) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.userId = comment.getUser().getId();
        this.nickname = comment.getUser().getNickname();
        this.profileImg = comment.getUser().getProfileImg();
        this.modifiedAt = comment.getModifiedDate().toString();
        this.commentReplyList = cmtReplyResponseDtoList;
    }


}

package com.project.dogfaw.comment.dto;

import com.project.dogfaw.comment.model.Comment;
import com.project.dogfaw.comment.model.CommentReply;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class CmtReplyResponseDto {
    private Long commentReplyId;
    private String content;
    private String modifiedAt;
    private Long userId;
    private String nickname;
    private String profileImg;

    public CmtReplyResponseDto(CommentReply commentReply) {
        this.commentReplyId = commentReply.getId();
        this.content = commentReply.getContent();
        this.userId = commentReply.getUser().getId();
        this.nickname = commentReply.getUser().getNickname();
        this.profileImg = commentReply.getUser().getProfileImg();
        this.modifiedAt = commentReply.getModifiedDate().toString();
    }
}

package com.project.dogfaw.comment.dto;

import com.project.dogfaw.comment.model.Comment;

public class CmtReplyResponseDto {
    private Long commentReplyId;
    private String content;
    private String modifiedAt;
    private Long userId;
    private String nickname;
    private String profileImg;

    public CmtReplyResponseDto(Comment comment) {
        this.commentReplyId = comment.getId();
        this.content = comment.getContent();
        this.userId = comment.getUser().getId();
        this.nickname = comment.getUser().getNickname();
        this.profileImg = comment.getUser().getProfileImg();
        this.modifiedAt = comment.getModifiedDate().toString();
    }
}

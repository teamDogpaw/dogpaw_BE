package com.project.dogfaw.comment.dto;

import com.project.dogfaw.comment.model.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private Long commentId;
    private String comment;
    private String modifiedAt;
    private Long userId;
    private String nickname;
    private String profileImg;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.comment = comment.getComment();
        this.userId = comment.getUser().getId();
        this.nickname = comment.getUser().getNickname();
        this.profileImg = comment.getUser().getProfileImg();
        this.modifiedAt = comment.getModifiedDate().toString();
    }
}

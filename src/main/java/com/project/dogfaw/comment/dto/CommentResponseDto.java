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
    private List<CommentReply> commentReplyList;

    private Long comment;

    private Long user;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.userId = comment.getUser().getId();
        this.nickname = comment.getUser().getNickname();
        this.profileImg = comment.getUser().getProfileImg();
        this.modifiedAt = comment.getModifiedDate().toString();
        this.commentReplyList = CommentReplyList(comment);
    }


    public List<CommentReply> CommentReplyList(Comment comment){
        List<CommentReply> commentReplylists = comment.getCommentReplyList();
        List<CommentReply> commentReplies = new ArrayList<>();
        for(CommentReply commentReply :commentReplylists ){
            this.comment = comment.getId();
            this.content = commentReply.getContent();
            this.nickname =commentReply.getUser().getNickname();
            this.profileImg = commentReply.getUser().getProfileImg();
            this.user = commentReply.getUser().getId();
            commentReplies.add(commentReply);
        }
        return commentReplies;
    }

}

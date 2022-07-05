package com.project.dogfaw.post.dto;

import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.user.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter

public class PostResponseDto {

    private final Long postId;
    private final String title;
    private final Boolean online;
    private final String stack;
    private final String period;
    private final int startAt;
    private final String content;

    private final boolean deadline;

    private final String nickname;

    private final String profileImg;

    private final int bookmarkCnt;

    private final int commentCnt;

    private final boolean bookMarkStatus;

    public PostResponseDto(Post post, boolean bookMarkStatus, User writer){

        this.postId = post.getId();
        this.title = post.getTitle();
        this.online = post.getOnline();
        this.stack = post.getStack();
        this.period = post.getPeriod();
        this.startAt = post.getStartAt();
        this.content = post.getContent();
        this.deadline = post.getDeadline();
        this.nickname = post.getNickname();
        this.profileImg = writer.getProfileImg();
        this.bookmarkCnt = post.getBookmarkCnt();
        this.commentCnt = post.getCommentCnt();
        this.bookMarkStatus = bookMarkStatus;

    }

}

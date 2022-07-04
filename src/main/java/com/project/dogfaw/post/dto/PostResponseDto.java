package com.project.dogfaw.post.dto;

import com.project.dogfaw.post.model.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PostResponseDto {

    private Long postId;
    private String title;
    private Boolean online;
    private String stack;
    private String period;
    private int startAt;
    private String content;

    private String nickname;

    private int maxCapacity;

    private int currentMember;

    private int bookmarkCnt;

    private int commentCnt;

    public PostResponseDto(Post post){
        this.postId = post.getId();
        this.title = post.getTitle();
        this.online = post.getOnline();
        this.stack = post.getStack();
        this.period = post.getPeriod();
        this.startAt = post.getStartAt();
        this.content = post.getContent();
        this.nickname = post.getNickname();
        this.maxCapacity = post.getMaxCapacity();
        this.currentMember = post.getCurrentMember();
        this.bookmarkCnt = post.getBookmarkCnt();
        this.commentCnt = post.getCommentCnt();

    }


    // this.title = title;
    //        this.online = online;
    //        this.stack = stack;
    //        this.period = period;
    //        this.startAt = startAt;
    //        this.content = content;
    //        this.deadline = deadline;
    //        this.nickname = nickname;
    //        this.maxCapacity = maxCapacity;
    //        this.currentMember = currentMember;
    //        this.bookmarkCnt = bookmarkCnt;
    //        this.commentCnt = commentCnt;
}

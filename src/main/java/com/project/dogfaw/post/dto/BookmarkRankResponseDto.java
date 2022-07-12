package com.project.dogfaw.post.dto;

import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.user.model.Stack;
import com.project.dogfaw.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter

public class BookmarkRankResponseDto {
    private Long postId;
    private String title;
    private String online;
    private List<Stack> stacks;
    private String period;
    private String startAt;
    private String content;

    private Boolean deadline;

    private String nickname;

    private String profileImg;

    private int bookmarkCnt;

    private int commentCnt;

    private boolean bookMarkStatus;


    public BookmarkRankResponseDto(Post post){
        this.postId = post.getId();
        this.title = post.getTitle();
        this.online = post.getOnline();
        this.stacks = post.getUser().getStacks();
        this.period = post.getPeriod();
        this.startAt = post.getStartAt();
        this.content = post.getContent();
        this.deadline = post.getDeadline();
        this.nickname = post.getUser().getNickname();
        this.profileImg = post.getUser().getProfileImg();
        this.bookmarkCnt = post.getBookmarkCnt();
        this.commentCnt = post.getCommentCnt();
        this.bookMarkStatus = false;
    }
}

package com.project.dogfaw.mypage.dto;

import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class MyApplyingResponseDto {

    private Long postId;
    private String title;
    private String online;
    private List<String> stacks;
    private String period;
    private String startAt;
    private String content;

    private Boolean deadline;

    private String nickname;

    private String profileImg;

    private int bookmarkCnt;

    private int commentCnt;

    private boolean bookMarkStatus;

    private int currentMember;

    private int maxCapacity;

    private int applierCnt;



    public MyApplyingResponseDto(Post post, List<String> stacks, boolean bookMarkStatus, User writer){
        this.postId = post.getId();
        this.title = post.getTitle();
        this.online = post.getOnline();
        this.stacks = stacks;
        this.period = post.getPeriod();
        this.startAt = post.getStartAt();
        this.content = post.getContent();
        this.deadline = post.getDeadline();
        this.nickname = writer.getNickname();
        this.profileImg = writer.getProfileImg();
        this.bookmarkCnt = post.getBookmarkCnt();
        this.commentCnt = post.getCommentCnt();
        this.bookMarkStatus = bookMarkStatus;
        this.currentMember = post.getCurrentMember();
        this.maxCapacity = post.getMaxCapacity();
        this.applierCnt = post.getUserApplications().size();
    }
}

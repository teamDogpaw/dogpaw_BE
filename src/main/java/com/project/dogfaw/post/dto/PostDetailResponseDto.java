package com.project.dogfaw.post.dto;

import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class PostDetailResponseDto {
    private Long id;
    private String title;
    private boolean onLine;
    private String stack;
    private String period;
    private int startAt;
    private int maxCapacity;
    private String content;
    private String nickname;
    private String profileImg;
    private boolean deadline;
    private boolean bookMarkStatus;

    public PostDetailResponseDto(Post post, User user, boolean bookMarkStatus){
        this.id = post.getId();
        this.title = post.getTitle();
        this.onLine = post.getOnline();
        this.stack = post.getStack();
        this.period = post.getPeriod();
        this.startAt = post.getStartAt();
        this.maxCapacity = post.getMaxCapacity();
        this.content = post.getContent();
        this.nickname = user.getNickname();
        this.profileImg = user.getProfileImg();
        this.deadline = post.getDeadline();
        this.bookMarkStatus = bookMarkStatus;

    }


}


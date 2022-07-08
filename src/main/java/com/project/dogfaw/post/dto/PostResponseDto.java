package com.project.dogfaw.post.dto;

import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PostResponseDto {

    private Long postId;
    private String title;
    private String online;
    private List<String> stacks;
    private String period;
    private int startAt;
    private String content;

    private Boolean deadline;

    private String nickname;

    private String profileImg;

    private int bookmarkCnt;

    private int commentCnt;

    private boolean bookMarkStatus;

    public PostResponseDto(Post post, List<String> stacks, boolean bookMarkStatus, User writer){

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

    }

}

package com.project.dogfaw.mypage.dto;

import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@Getter
public class MypageResponseDto {

    private Long postId;
    private String title;
    private Boolean online;
    private String stack;
    private String period;
    private int startAt;
    private String content;

    private boolean deadline;

    private String nickname;

    private String profileImg;

    private int bookmarkCnt;

    private int commentCnt;


    public MypageResponseDto(Post post, User writer){

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

    }
}

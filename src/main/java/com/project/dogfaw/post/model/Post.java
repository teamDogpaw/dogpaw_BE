package com.project.dogfaw.post.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Post extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Boolean online;

    @Column(nullable = false)
    private String stack;

    @Column(nullable = false)
    private String period;

    @Column(nullable = false)
    private int startAt;

    @Column(length = 400, nullable = false)
    private String content;

    @Column(nullable = false)
    private int maxCapacity;

    @Column(nullable = false)
    private int currentMember;

    @Column(nullable = false)
    private int deadline;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private int bookmarkCnt;

    @Column(nullable = false)
    private int commentCnt;

    public Post(String title, Boolean online, String stack, String period, int startAt, String content, int deadline, String nickname, int maxCapacity, int currentMember, int bookmarkCnt, int commentCnt) {
        this.title = title;
        this.online = online;
        this.stack = stack;
        this.period = period;
        this.startAt = startAt;
        this.content = content;
        this.deadline = deadline;
        this.nickname = nickname;
        this.maxCapacity = maxCapacity;
        this.currentMember = currentMember;
        this.bookmarkCnt = bookmarkCnt;
        this.commentCnt = commentCnt;

    }


    
    //참여신청시 +1, 참여취소시 -1(건영)
    //현재모집인원 +1
    public void increaseCnt() {this.currentMember += 1;}
    //현재모집인원 -1
    public void decreaseCnt() {
        this.currentMember -= 1;
    }

}

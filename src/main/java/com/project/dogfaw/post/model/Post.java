package com.project.dogfaw.post.model;

import com.project.dogfaw.post.dto.PostRequestDto;
import com.project.dogfaw.user.model.User;
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

    @ManyToOne
    @JoinColumn(name="userId", nullable = false)
    private User user;
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

    @Column
    private int currentMember;

    @Column
    private Boolean deadline;

    @Column(nullable = false)
    private String nickname;

    @Column
    private int bookmarkCnt;

    @Column
    private int commentCnt;


//    public Post(String title, Boolean online, String stack, String period, int startAt, String content, int deadline, String nickname, int maxCapacity, int currentMember, int bookmarkCnt, int commentCnt) {
//        this.title = title;
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
//
//    }

    public Post(PostRequestDto postRequestDto, User user) {
        this.title = postRequestDto.getTitle();
        this.online = postRequestDto.getOnline();
        this.stack = postRequestDto.getStack();
        this.period = postRequestDto.getPeriod();
        this.startAt = postRequestDto.getStartAt();
        this.content = postRequestDto.getContent();
        this.user = user;
        }



    //참여신청시 +1, 참여취소시 -1(건영)
    //현재모집인원 +1
    public void increaseCnt() {this.currentMember += 1;}
    //현재모집인원 -1
    public void decreaseCnt() {this.currentMember -= 1;}

    //유저가 북마크시 해당 게시글 북마크 수 +1, 취소시 -1
    public void increaseBmCount(){this.bookmarkCnt += 1;}
    public void decreaseBmCount(){this.bookmarkCnt -= 1;}


    //모집마감 || 모집중
    public void isDeadline(){this.deadline = true;}
    public void isOngoing(){this.deadline = false;}


}

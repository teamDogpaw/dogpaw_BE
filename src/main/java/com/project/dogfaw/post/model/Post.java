package com.project.dogfaw.post.model;


import com.project.dogfaw.bookmark.repository.BookMarkRepository;
import com.project.dogfaw.post.dto.PostRequestDto;
import com.project.dogfaw.user.model.Stack;
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
    private String period;

    @Column(nullable = false)
    private int startAt;

    @Column(nullable = false)
    private int maxCapacity;

    @Column(length = 400, nullable = false)
    private String content;

    @Column
    private int currentMember;

    @Column
    private Boolean deadline;

//    @Column
//    private String nickname;
//
//    @Column
//    private String profileImg;

    @Column
    private int bookmarkCnt;

    @Column
    private int commentCnt;

//    @OneToMany
//    @JoinColumn(name = "poststack_id")
//    private List<PostStack> stacks = new ArrayList<>();




    public Post(PostRequestDto postRequestDto, User user) {
        this.title = postRequestDto.getTitle();
        this.online = postRequestDto.getOnline();
        this.period = postRequestDto.getPeriod();
        this.startAt = postRequestDto.getStartAt();
        this.maxCapacity = postRequestDto.getMaxCapacity();
        this.content = postRequestDto.getContent();
//        this.profileImg = getProfileImg();
        this.user = user;
        }




    public void update(PostRequestDto postRequestDto, Long id) {
        this.title = postRequestDto.getTitle();
        this.online = postRequestDto.getOnline();
        this.period = postRequestDto.getPeriod();
        this.startAt = postRequestDto.getStartAt();
        this.maxCapacity = postRequestDto.getMaxCapacity();
        this.content = postRequestDto.getContent();

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



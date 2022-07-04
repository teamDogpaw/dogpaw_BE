package com.project.dogfaw.post.model;

import com.sun.org.apache.xpath.internal.operations.Bool;
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

    @Column(nullable = true)
    private Boolean online;

    @Column(nullable = true)
    private String stack;

    @Column(nullable = false)
    private String period;

    @Column(nullable = false)
    private int startAt;

    @Column(nullable = true)
    private int currentMember;

    @Column(nullable = false)
    private int maxCapacity;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String creatAt;

    @Column(nullable = false)
    private String modifiedAt;

    @Column(nullable = false)
    private String deadline;

    @Column(nullable = false)
    private String username;






    //참가신청시 현재 모집인원 + 또는 - 하기위한 생성자(건영)
    //모집인원 +1
    public void increaseCnt() {
        this.currentMember += 1;
    }
    //모집인원 -1
    public void decreaseCnt() {
        this.currentMember -= 1;
    }

}

//”id” : postId,
//”title”: post 제목,
//”online”: 진행 방식 (온/오프라인),
//”stack”: ,
//”period”: ,
//”startAt”: ,
//”content”: ,
//”deadline”: ,
//”nickname”: ,
//”profileImg”:
//”bookmarkCnt ” :
//”commentCnt” :

package com.project.dogfaw.comment.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.common.Timestamped;
import com.project.dogfaw.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.util.List;

@RestController
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Comment extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String profileImg;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    // FK로 POST_ID 들어옴.
    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @OneToMany(mappedBy = "comment", orphanRemoval = true) //대댓글 딸려오는거 다 지워주기
    @JsonManagedReference(value="commentReply-comment-FK")
    private List<CommentReply> commentReplyList;

    public Comment(String content, String nickname, String profileImg, User user, Post post) {
        this.content = content;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.user = user;
        this.post = post;

    }

    //코멘트 수정
    public void updateComment(String cmt) {
        this.content = cmt;

    }

}
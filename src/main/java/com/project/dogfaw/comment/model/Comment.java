package com.project.dogfaw.comment.model;

import com.project.dogfaw.comment.dto.CommentPutDto;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.model.Timestamped;
import com.project.dogfaw.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Comment extends Timestamped {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String profileImg;

    // FK로 USER_ID 들어옴.
    @ManyToOne //ID 유저네임? 그 이아디?
    @JoinColumn(name = "USER_ID")
    private User user;

    // FK로 POST_ID 들어옴.
    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;



    public Comment(String content, String nickname, String profileImg, User user, Post post) {
        this.content = content;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.user = user;
        this.post = post;
    }

    //코멘트 수정
    public void updateComment(CommentPutDto requestDto) {
        this.content = requestDto.getContent();

    }
}
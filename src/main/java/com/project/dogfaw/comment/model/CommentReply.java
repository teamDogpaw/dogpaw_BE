package com.project.dogfaw.comment.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.dogfaw.comment.dto.CommentPutDto;
import com.project.dogfaw.common.Timestamped;
import com.project.dogfaw.user.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;

@RequiredArgsConstructor
@RestController
@Getter
@Setter
@Entity

public class CommentReply extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "COMMENT_ID")
    @JsonBackReference(value = "commentReply-comment-FK")
    private Comment comment;

    @Column(nullable = false)
    private String content;

    @ManyToOne //ID 유저네임? 그 이아디?
    @JoinColumn(name = "USER_ID")
//    @JsonIgnore
    private User user;



    public CommentReply(String content, User user, Comment comment) {
        this.content = content;
        this.user = user;
        this.comment = comment;
    }
    public void updateCommentReply(CommentPutDto requestDto) {
        this.content = requestDto.getContent();

    }
}

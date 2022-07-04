package com.project.dogfaw.bookmark.model;

import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.user.Member;
import com.project.dogfaw.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class BookMark {
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Id
    private Long id;


    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn
    private Post post;


    public BookMark(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}

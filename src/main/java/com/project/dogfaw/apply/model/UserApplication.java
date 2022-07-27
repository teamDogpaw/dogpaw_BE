package com.project.dogfaw.apply.model;

import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class UserApplication {
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn(name = "P_ID")
    private Post post;

    public UserApplication(User user, Post post) {
        this.user = user;
        this.post = post;

    }
}


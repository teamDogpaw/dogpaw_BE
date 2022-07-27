package com.project.dogfaw.acceptance.model;

import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
public class Acceptance {
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Id
    private Long id;


    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Post post;

    public Acceptance(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}

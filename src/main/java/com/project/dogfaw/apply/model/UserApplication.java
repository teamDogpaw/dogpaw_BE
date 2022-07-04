package com.project.dogfaw.apply.model;

import com.project.dogfaw.post.Post;
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
    private Member member;

    @ManyToOne
    @JoinColumn
    private Post post;


    public UserApplication(Member member, Post post) {
        this.member = member;
        this.post = post;
    }
}

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
    @JoinColumn
    private Post post;

    private int cnt;


    //모집인원 +1
    public void increaseCnt() {this.cnt += 1;}
    //모집인원 -1
    public void decreaseCnt() {
        this.cnt -= 1;
    }
}

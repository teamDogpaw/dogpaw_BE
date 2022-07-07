package com.project.dogfaw.post.model;

import com.project.dogfaw.user.dto.StackDto;
import com.project.dogfaw.user.model.Stack;
import com.project.dogfaw.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@DynamicUpdate // null 값인 field 를 DB에서 설정된 default을 줌
public class PostStack {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String stack;

    private Long postId;




    public PostStack(Long postId, String stack) {
        this.postId = postId;
        this.stack = stack;
    }

    public PostStack(StackDto stackdto, Post post) {
        this.stack = stackdto.getStack();
        this.postId = post.getId();
    }

}

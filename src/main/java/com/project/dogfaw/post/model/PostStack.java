package com.project.dogfaw.post.model;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.project.dogfaw.user.dto.StackDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
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

    @Column
    private Long postId;


    public PostStack(Long postId, String stack) {
        this.postId = postId;
        this.stack = stack;
    }

    public void updatePostStack(String stack, Long postId) {
        this.stack = stack;
        this.postId = postId;
    }


}

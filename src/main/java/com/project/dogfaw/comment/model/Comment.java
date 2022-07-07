package com.project.dogfaw.comment.model;

import com.project.dogfaw.comment.dto.CommentRequestDto;
import com.project.dogfaw.common.exception.CustomException;
import com.project.dogfaw.common.exception.ErrorCode;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.model.Timestamped;
import com.project.dogfaw.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Comment extends Timestamped {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int startAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User user;

    // comment 등록
    public Comment(Post post, CommentRequestDto requestDto, User user) {
//        if (!StringUtils.hasText(requestDto.getComment())) {
//            throw new CustomException(ErrorCode.COMMENT_WRONG_INPUT);
//        }

        this.post = post;
        this.content = requestDto.getContent();
        this.user = user;
        this.startAt = getStartAt();
    }


    // comment 수정
    public void updateComment(CommentRequestDto requestDto) {
//        if (!StringUtils.hasText(requestDto.getComment())) {
//            throw new CustomException(ErrorCode.COMMENT_WRONG_INPUT);
//        }
        this.content = requestDto.getContent();
        this.startAt = getStartAt();
    }

}